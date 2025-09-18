package kr.co.ibk.service;

import kr.co.ibk.common.utils.FileUtilHelper;
import kr.co.ibk.domain.enums.*;
import kr.co.ibk.domain.web.*;
import kr.co.ibk.model.BillLearningDataForm;
import kr.co.ibk.model.CardLearningDataForm;
import kr.co.ibk.model.LearningModelForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.*;
import kr.co.ibk.web.BaseCont;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningModelService extends BaseCont {

    private final LearningDataRepository learningDataRepository;
    @Value("${Globals.fileStorePath}")
    private String filepath;

    private final LearningModelRepository learningModelRepository;
    private final LearningModelInputRepository learningModelInputRepository;
    private final CardLearningDataRepository cardLearningDataRepository;
    private final BillLearningDataRepository billLearningDataRepository;
    private final TemplateRepository templateRepository;
    private final TemplateInputRepository templateInputRepository;
    private final MccService mccService;

    public HashMap<String, Object> save(LearningModelForm form, MemberInfo memberInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "FAIL");

        Long saveCnt = 0L;

        form.setRegId(memberInfo.getMemId());
        form.setModId(memberInfo.getMemId());

        boolean isInsert = false;
        if (ObjectUtils.isEmpty(form.getId())) {
            //insert
            isInsert = true;
            learningModelRepository.insert(form);
        } else {
            //update
            learningModelRepository.update(form);

            learningModelInputRepository.delete(form.getId());
        }

        saveCnt = learningModelInputRepository.insertList(form.getId(), form.getInputArr(), InOutGbnType.INPUT.name());
        saveCnt += learningModelInputRepository.insertList(form.getId(), form.getOutputArr(), InOutGbnType.OUTPUT.name());

        /*if (isInsert &&
                !ObjectUtils.isEmpty(form.getTemplateAt()) && "Y".equals(form.getTemplateAt())) {
            //template save
            TemplateForm tempForm = new TemplateForm();
            tempForm.setTemplateName(form.getTemplateName());
            tempForm.setSelectCon("");
            tempForm.setHdqrBobDcd(form.getHdqrBobDcd());
            tempForm.setMemId(memberInfo.getMemId());
            templateRepository.insert(tempForm);

            saveCnt = templateInputRepository.insertList(tempForm.getId(), form.getInputArr(), InOutGbnType.INPUT.name());
            saveCnt += templateInputRepository.insertList(tempForm.getId(), form.getOutputArr(), InOutGbnType.OUTPUT.name());
        }*/

        if (saveCnt > 0) {
            map.put("status", "SUCCESS");
        }
        return map;
    }

    /**
     * 모델 학습
     * 1. 학습 모델의 상태 "학습 데이터 생성 중" 업데이트 후 SUCESS 리턴
     * 2. 새로운 스레드 생성
     *   2-1. 모델의 학습 타입(BC 카드 또는 세금계산서)에 따라 학습 데이터 조회
     *       - 조회된 데이터로 학습 데이터 파일을 생성하고, 파일경로/파일명 업데이트
     *       - 이 과정 중 오류가 발생할 경우, 학습 모델의 상태를 "학습 데이터 생성 오류"로 업데이트
     *   2-2. 모델 학습 API를 호출
     * @return 학습 상태를 나타내는 HashMap. 상태는 SUCCESS로 초기화되어 반환됨.
     */
    @Transactional
    public HashMap<String, Object> learning(LearningModelForm form) {
        // 1. 학습 모델의 상태 "학습 데이터 생성 중" 업데이트 후 SUCESS 리턴
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "SUCCESS");
        form.setDeployStatus(DeployStatusType.LEARN_DATA_ING.getCode().toString());
        learningModelRepository.updateStatus(form);

        LearningModelInfo load = learningModelRepository.getLoad(form.getId());
        map.put("learnName", load.getLearnName());
        new Thread(() -> {
            try {
                List<LearningModelInputInfo> list = learningModelInputRepository.getList(form.getId());
                Map<String, StringBuilder> fileCon = new HashMap<>();

                // 2-1. 모델의 학습 타입(BC 카드 또는 세금계산서)에 따라 학습 데이터 조회
                if (load.getLearningType().equals(LearningType.CARD)) {
                    CardLearningDataForm learningDataForm = new CardLearningDataForm();
                    if (!ObjectUtils.isEmpty(load.getSelectCon())) { // 학습 모델에 저장된 선택 조건을 가져와 카드 학습 데이터 폼에 재설정
                        HashMap<String, String> searchJsonMap = jsonToHashMap(load.getSelectCon());
                        learningDataForm.setSearchStartDate(searchJsonMap.remove("searchStartDate"));
                        learningDataForm.setSearchEndDate(searchJsonMap.remove("searchEndDate"));
                        learningDataForm.setSearchTarget(searchJsonMap.remove("searchTarget"));
                        learningDataForm.setSearchJsonMap(searchJsonMap);

                        learningDataForm.setSearchRegex(makeSearchQuery(learningDataForm.getSearchJsonMap(), 0));
                    }
                    // 카드 학습데이터 목록 조회 > 파일 내용 생성
                    List<CardLearningDataInfo> dataList = cardLearningDataRepository.getLearningList(learningDataForm);
                    fileCon = cardLearningFileContent(dataList, list); // 파일 내용 생성

                } else if (load.getLearningType().equals(LearningType.BILL)) {
                    BillLearningDataForm learningDataForm = new BillLearningDataForm();
                    if (!ObjectUtils.isEmpty(load.getSelectCon())) {  // 학습 모델에 저장된 선택 조건을 가져와 세금계산서 학습 데이터 폼에 재설정
                        HashMap<String, String> searchJsonMap = jsonToHashMap(load.getSelectCon());
                        learningDataForm.setSearchStartDate(searchJsonMap.remove("searchStartDate"));
                        learningDataForm.setSearchEndDate(searchJsonMap.remove("searchEndDate"));
                        //learningDataForm.setSearchTarget(searchJsonMap.remove("searchTarget"));
                        searchJsonMap.remove("searchTarget");
                        learningDataForm.setSearchJsonMap(searchJsonMap);

                        learningDataForm.setSearchRegex(makeSearchQuery(learningDataForm.getSearchJsonMap(), 0));
                    }
                    List<BillLearningDataInfo> dataList = billLearningDataRepository.getLearningList(learningDataForm);
                    fileCon = billLearningFileContent(dataList, list); // 파일 내용 생성
                }

                // - 조회된 데이터로 학습 데이터 파일을 생성하고, 지정된 파일경로/파일명 업데이트
                StringBuilder header = fileCon.get("header");
                StringBuilder body = fileCon.get("body");

                String filePath = filepath + File.separator + "learning" + File.separator + form.getId();
                File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".txt";
                File file = new File(filePath + File.separator + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(fw);
                writer.write(header.append(body).toString());
                writer.close();

                LearningModelForm update = new LearningModelForm();
                update.setId(form.getId());
                update.setFilePath(filePath);
                update.setFileName(fileName);
                update.setEpoch(form.getEpoch());
                update.setLearningRate(form.getLearningRate());
                update.setBatchSize(form.getBatchSize());
                learningModelRepository.updateFile(update);

                // 2-2. 모델 학습 API를 호출
                boolean result = mccService.trainModel(form.getId());
                if (!result) { // 실패한 경우, 학습 모델의 상태를 "학습 오류"로 업데이트
                    try {
                        form.setDeployStatus(DeployStatusType.LEARN_ERROR.getCode().toString());
                        learningModelRepository.updateStatus(form);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                try {
                    // 2번 과정 중 오류가 발생할 경우, 학습 모델의 상태를 "학습 데이터 생성 오류"로 업데이트
                    form.setDeployStatus(DeployStatusType.LEARN_DATA_ERROR.getCode().toString());
                    learningModelRepository.updateStatus(form);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        return map;
    }

    @Transactional
    public Map<String, StringBuilder> cardLearningFileContent(List<CardLearningDataInfo> dataList, List<LearningModelInputInfo> list) {
        Map<String, StringBuilder> fileContent = new HashMap<>();

        String sep = "<<|SEP|>>AMSL_AMT";
        // 헤더
        StringBuilder header = new StringBuilder();
        String separator = " ";
        boolean isAmslAmt = false;
        boolean outputFirst = true;
        for (LearningModelInputInfo info : list) {
            String colNm = null;
            if (InOutGbnType.INPUT.equals(info.getInoutGbn())) {
                separator = " || ";
                InputColumnCardType inputColumnType = InputColumnCardType.valueOf(info.getColName());
                if (InputColumnCardType.AMSL_AMT.equals(inputColumnType)) {
                    isAmslAmt = true;
                } else {
                    colNm = inputColumnType.name();
                }
            } else {
                if (outputFirst) {
                    header.append(sep);
                    outputFirst = false;
                }
                separator = "\t";
                OutputColumnCardType outputColumnType = OutputColumnCardType.valueOf(info.getColName());
                if (OutputColumnCardType.BDGT_BSNS_FRCS_CON.equals(outputColumnType)) {
                    colNm = InputColumnCardType.BRCD.name() + "-" + outputColumnType.name();
                } else {
                    colNm = outputColumnType.name();
                }
            }
            if (!header.toString().isEmpty() && colNm != null) {
                header.append(separator);
            }
            if (colNm != null) {
                header.append(colNm);
            }
        }
        if (!isAmslAmt) {
            String replace = header.toString().replace(sep, "");
            header = new StringBuilder();
            header.append(replace);
        }
        header.append("\n");

        // 바디
        StringBuilder body = new StringBuilder();
        outputFirst = true;
        separator = " || ";
        for (CardLearningDataInfo data : dataList) {
            boolean first = true;
            for (LearningModelInputInfo info : list) {
                if (InOutGbnType.INPUT.equals(info.getInoutGbn())) {
                    InputColumnCardType inputColumnType = InputColumnCardType.valueOf(info.getColName());
                    separator = " || ";
                    if (!first && !InputColumnCardType.AMSL_AMT.equals(inputColumnType)) {
                        body.append(separator);
                    }
                    String value = null;
                    if (InputColumnCardType.BRCD.equals(inputColumnType)) {
                        value = data.getBrcd();
                    } else if (InputColumnCardType.CDN.equals(inputColumnType)) {
                        value = data.getCdn();
                    } else if (InputColumnCardType.BDGT_TSTM_USE_HMS.equals(inputColumnType)) {
                        value = data.getBdgtTstmUseHms();
                    } else if (InputColumnCardType.AMSL_AMT.equals(inputColumnType)) {
//                            value = String.valueOf(data.getAmslAmt());
                    } else if (InputColumnCardType.AFST_NM.equals(inputColumnType)) {
                        value = data.getAfstNm();
                    } else if (InputColumnCardType.TPBS_NM.equals(inputColumnType)) {
                        value = data.getTpbsNm();
                    } else if (InputColumnCardType.BZDY_YN.equals(inputColumnType)) {
                        value = data.getBzdyYn();
                    } else if (InputColumnCardType.AFST_DTL_ADR.equals(inputColumnType)) {
                        value = data.getAfstDtlAdr();
                    } else if (InputColumnCardType.BRNC_ADR.equals(inputColumnType)) {
                        value = data.getBrncAdr();
                    } else if (InputColumnCardType.AFST_BZN.equals(inputColumnType)) {
                        value = data.getAfstBzn();
                    } else if (InputColumnCardType.AMSL_AFST_NO.equals(inputColumnType)) {
                        value = data.getAmslAfstNo();
                    } else if (InputColumnCardType.AFST_TPBCD.equals(inputColumnType)) {
                        value = data.getAfstTpbcd();
                    }
                    if (value != null) {
                        body.append(inputColumnType.name())
                                .append(" : ")
                                .append(value.replaceAll("\r", "").replaceAll("\n", "").trim());
                    }
                } else {
                    if (outputFirst && isAmslAmt) {
                        body.append(sep.replace("AMSL_AMT", String.valueOf(data.getAmslAmt())));
                        outputFirst = false;
                    }
                    separator = "\t";
                    if (!first) {
                        body.append(separator);
                    }
                    String value = null;
                    OutputColumnCardType outputColumnType = OutputColumnCardType.valueOf(info.getColName());
                    if (OutputColumnCardType.BDMN_ITEX_MNGM_NO.equals(outputColumnType)) {
                        value = data.getBdmnItexMngmNo();
                    } else if (OutputColumnCardType.BDGT_PRFR_RSN_FRCS_CON.equals(outputColumnType)) {
                        value = data.getBdgtPrfrRsnFrcsCon();
                    } else if (OutputColumnCardType.BDGT_BSNS_FRCS_CON.equals(outputColumnType)) {
                        value = data.getBrcd() + "-" + data.getBdgtBsnsFrcsCon();
                    }
                    body.append(value.replaceAll("\r", "").replaceAll("\n", "").trim());
                }
                first = false;
            }
            outputFirst = true;
            body.append("\n");
        }
        fileContent.put("header", header);
        fileContent.put("body", body);

        return fileContent;
    }

    @Transactional
    public Map<String, StringBuilder> billLearningFileContent(List<BillLearningDataInfo> dataList, List<LearningModelInputInfo> list) {
        Map<String, StringBuilder> fileContent = new HashMap<>();

        String sep = "<<|SEP|>>ISS_AMT";
        // 헤더
        StringBuilder header = new StringBuilder();
        String separator = " ";
        boolean isIssAmt = false;
        boolean outputFirst = true;
        for (LearningModelInputInfo info : list) {
            String colNm = null;
            if (InOutGbnType.INPUT.equals(info.getInoutGbn())) {
                separator = " || ";
                InputColumnBillType inputColumnType = InputColumnBillType.valueOf(info.getColName());
                if (InputColumnBillType.ISS_AMT.equals(inputColumnType)) {
                    isIssAmt = true;
                } else {
                    colNm = inputColumnType.name();
                }
            } else {
                if (outputFirst) {
                    header.append(sep);
                    outputFirst = false;
                }
                separator = "\t";
                OutputColumnBillType outputColumnType = OutputColumnBillType.valueOf(info.getColName());
                if (OutputColumnBillType.BDGT_BSNS_FRCS_CON.equals(outputColumnType)) {
                    colNm = InputColumnBillType.BRCD.name() + "-" + outputColumnType.name();
                } else {
                    colNm = outputColumnType.name();
                }
            }
            if (!header.toString().isEmpty() && colNm != null) {
                header.append(separator);
            }
            if (colNm != null) {
                header.append(colNm);
            }
        }
        if (!isIssAmt) {
            String replace = header.toString().replace(sep, "");
            header = new StringBuilder();
            header.append(replace);
        }
        header.append("\n");

        // 바디
        StringBuilder body = new StringBuilder();
        outputFirst = true;
        separator = " || ";
        for (BillLearningDataInfo data : dataList) {
            boolean first = true;
            for (LearningModelInputInfo info : list) {
                if (InOutGbnType.INPUT.equals(info.getInoutGbn())) {
                    InputColumnBillType inputColumnType = InputColumnBillType.valueOf(info.getColName());
                    separator = " || ";
                    if (!first && !InputColumnBillType.ISS_AMT.equals(inputColumnType)) {
                        body.append(separator);
                    }
                    String value = null;
                    if (InputColumnBillType.BRCD.equals(inputColumnType)) {
                        value = data.getBrcd();
                    } else if (InputColumnBillType.TXBL_DCD.equals(inputColumnType)) {
                        value = data.getTxblDcd();
                    } else if (InputColumnBillType.SPRL_BSNN_NO.equals(inputColumnType)) {
                        value = data.getSplrBsnnNo();
                    } else if (InputColumnBillType.ISS_AMT.equals(inputColumnType)) {
                        //value = String.valueOf(data.getIssAmt());
                    } else if (InputColumnBillType.SPLR_FRM.equals(inputColumnType)) {
                        value = data.getSplrFrm();
                    } else if (InputColumnBillType.SPLR_BZST_NM.equals(inputColumnType)) {
                        value = data.getSplrBzstNm();
                    } else if (InputColumnBillType.SPLR_ITMS_NM.equals(inputColumnType)) {
                        value = data.getSplrItmsNm();
                    } else if (InputColumnBillType.TXBL_LSAR_NM.equals(inputColumnType)) {
                        value = data.getTxblLsarNm();
                    }
                    if (value != null) {
                        body.append(inputColumnType.name())
                                .append(" : ")
                                .append(value.replaceAll("\r", "").replaceAll("\n", "").trim());
                    }
                } else {
                    if (outputFirst && isIssAmt) {
                        String issAmtStr = sep.replace("ISS_AMT", String.valueOf(data.getIssAmt()));
                        body.append(issAmtStr);
                        outputFirst = false;
                    }
                    separator = "\t";
                    if (!first) {
                        body.append(separator);
                    }
                    String value = null;
                    OutputColumnBillType outputColumnType = OutputColumnBillType.valueOf(info.getColName());
                    if (OutputColumnBillType.BDGT_ITEX_FRCS_CON.equals(outputColumnType)) {
                        value = data.getBdmnItexMngmNo();
                    } else if (OutputColumnBillType.BDGT_PRFR_RSN_FRCS_CON.equals(outputColumnType)) {
                        value = data.getBdgtPrfrRsnFrcsCon();
                    } else if (OutputColumnBillType.BDGT_BSNS_FRCS_CON.equals(outputColumnType)) {
                        value = data.getBrcd() + "-" + data.getBdgtBsnsFrcsCon();
                    } else if (OutputColumnBillType.BDGT_EXNS_PAMT_MCD.equals(outputColumnType)) {
                        value = !ObjectUtils.isEmpty(data.getBdgtExnsPamtMcd()) ? data.getBdgtExnsPamtMcd() : "";
                    } else if (OutputColumnBillType.ACIM_CON.equals(outputColumnType)) {
                        value = !ObjectUtils.isEmpty(data.getAcimCon()) ? data.getAcimCon() : "";
                    }
                    body.append(value.replaceAll("\r", "").replaceAll("\n", "").trim());
                }
                first = false;
            }
            outputFirst = true;
            body.append("\n");
        }
        fileContent.put("header", header);
        fileContent.put("body", body);

        return fileContent;
    }

    public List<LearningModelInfo> getList(LearningModelForm params) {
        List<LearningModelInfo> modelList = Collections.emptyList();

        /*if (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate())) {
            params.setSearchStartDate(String.valueOf(LocalDate.now().minusMonths(1)).replaceAll("-", "."));
            params.setSearchEndDate(String.valueOf(LocalDate.now()).replaceAll("-", "."));
        }*/

        int totalCount = learningModelRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);

        params.setPaginationInfo(paginationInfo);

        if (totalCount > 0) {
            if (ObjectUtils.isEmpty(params.getSorting())) {
                params.setSorting("desc");
            }
            params.setPagingAt("Y");
            modelList = learningModelRepository.getList(params);
        }

        return modelList;
    }

    public LearningModelInfo getLoad(LearningModelForm form) {
        //model
        LearningModelInfo info = learningModelRepository.getLoad(form.getId());

        //model input list set
        String learningType = info.getLearningType() != null ? info.getLearningType().getName() : null;
        info.setInputList(learningModelInputRepository.getPartList(form.getId(), InOutGbnType.INPUT.name(), learningType));
        info.setOutputList(learningModelInputRepository.getPartList(form.getId(), InOutGbnType.OUTPUT.name(), learningType));
        return info;
    }

    public int modelNmCount(LearningModelForm form) {
        return learningModelRepository.modelNmCount(form.getLearnName());
    }

    @Transactional
    public HashMap<String, Object> delete(LearningModelForm form, MemberInfo memberInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "FAIL");

        Long deleteCnt = 0L;
        Integer[] idArr = form.getIdArr();

        try {
            if (!ObjectUtils.isEmpty(form.getIdArr()) && form.getIdArr().length > 0) {
                // 삭제 불가능한 배포 상태값 check
                int modelCnt = learningModelRepository.countByInIDAndDeployArr(idArr, DeployStatusType.getUndeletableList()
                        .stream()
                        .map(DeployStatusType::getCode)
                        .toArray(Integer[]::new));

                if (modelCnt > 0) {
                    return map;
                }

                // 모델 폴더 삭제
                for (Integer id : idArr) {
                    boolean result = FileUtilHelper.removeDirectory("models", String.valueOf(id));
                    if (!result) {
                        return map;
                    }
                }

                // 모델 삭제
                deleteCnt = learningModelRepository.deleteAllById(idArr, memberInfo.getMemId());
                if (deleteCnt > 0) {
                    map.put("status", "SUCCESS");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
        return map;
    }

    public List<LearningModelInfo> getTargetList(LearningType learningType, Integer hdqrBobDcd) {
        Integer[] deployArr = DeployStatusType.getDeployStatusList()
                .stream()
                .map(DeployStatusType::getCode)
                .toArray(Integer[]::new);

        return learningModelRepository.getTargetList(learningType, hdqrBobDcd, deployArr);
    }
}

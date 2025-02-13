package kr.co.ibk.service;

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

    @Transactional
    public HashMap<String, Object> learning(LearningModelForm form) {
        HashMap<String, Object> map = new HashMap<>();
        LearningModelInfo load = learningModelRepository.getLoad(form.getId());
        map.put("learnName", load.getLearnName());
        try {
            List<LearningModelInputInfo> list = learningModelInputRepository.getList(form.getId());
            Map<String, StringBuilder> fileCon = new HashMap<>();
            if (load.getLearningType().equals(LearningType.CARD)) {

                CardLearningDataForm learningDataForm = new CardLearningDataForm();
                if (!ObjectUtils.isEmpty(load.getSelectCon())) {
                    learningDataForm.setSearchJsonMap(jsonToHashMap(load.getSelectCon()));
                }
                List<CardLearningDataInfo> dataList = cardLearningDataRepository.getLearningList(learningDataForm);
                fileCon = cardLearningFileContent(dataList, list);

            } else if (load.getLearningType().equals(LearningType.BILL)) {
                BillLearningDataForm learningDataForm = new BillLearningDataForm();
                if (!ObjectUtils.isEmpty(load.getSelectCon())) {
                    learningDataForm.setSearchJsonMap(jsonToHashMap(load.getSelectCon()));
                }
                List<BillLearningDataInfo> dataList = billLearningDataRepository.getLearningList(learningDataForm);
                fileCon = billLearningFileContent(dataList, list);
            }

            StringBuilder header = fileCon.get("header");
            StringBuilder body = fileCon.get("body");

            System.out.println(header.toString());
            System.out.println(body.toString());

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

            mccService.trainModel(form.getId());

            map.put("status", "SUCCESS");
        } catch (Exception e) {
            try {
                form.setDeployStatus(DeployStatusType.LEARN_DATA_ERROR.getCode().toString());
                learningModelRepository.updateStatus(form);
                map.put("status", "FAIL");
            } catch (Exception ex) {
                map.put("status", "FAIL");
            }
        }
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
                                .append(value);
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
                    body.append(value);
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

        String sep = "<<|SEP|>>AMSL_AMT";
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
                                .append(value);
                    }
                } else {
                    if (outputFirst && isIssAmt) {
                        body.append(sep.replace("ISS_AMT", String.valueOf(data.getIssAmt())));
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
                    }
                    body.append(value);
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

    public HashMap<String, Object> delete(LearningModelForm form, MemberInfo memberInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "FAIL");

        Long deleteCnt;
        if (!ObjectUtils.isEmpty(form.getIdArr()) && form.getIdArr().length > 0) {
            deleteCnt = learningModelRepository.deleteAllById(form.getIdArr(), memberInfo.getMemId());
            if (deleteCnt > 0) {
                map.put("status", "SUCCESS");
            }
        }
        return map;
    }
}

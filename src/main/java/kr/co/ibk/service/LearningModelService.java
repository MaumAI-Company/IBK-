package kr.co.ibk.service;

import kr.co.ibk.domain.enums.InOutGbnType;
import kr.co.ibk.domain.enums.InputColumnType;
import kr.co.ibk.domain.enums.OutputColumnType;
import kr.co.ibk.domain.web.CardLearningDataInfo;
import kr.co.ibk.domain.web.LearningModelInfo;
import kr.co.ibk.domain.web.LearningModelInputInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.LearningModelForm;
import kr.co.ibk.model.SearchForm;
import kr.co.ibk.model.TemplateForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningModelService extends _BaseService {

    @Value("${Globals.fileStorePath}")
    private String filepath;

    private final LearningModelRepository learningModelRepository;
    private final LearningModelInputRepository learningModelInputRepository;
    private final CardLearningDataRepository cardLearningDataRepository;
    private final TemplateRepository templateRepository;
    private final TemplateInputRepository templateInputRepository;

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
            List<CardLearningDataInfo> dataList = cardLearningDataRepository.getLearningList(new SearchForm());

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
                    InputColumnType inputColumnType = InputColumnType.valueOf(info.getColName());
                    if (InputColumnType.AMSL_AMT.equals(inputColumnType)) {
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
                    OutputColumnType outputColumnType = OutputColumnType.valueOf(info.getColName());
                    if (OutputColumnType.BDGT_BSNS_FRCS_CON.equals(outputColumnType)) {
                        colNm = InputColumnType.BRCD.name() + "-" + outputColumnType.name();
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
                        InputColumnType inputColumnType = InputColumnType.valueOf(info.getColName());
                        separator = " || ";
                        if (!first && !InputColumnType.AMSL_AMT.equals(inputColumnType)) {
                            body.append(separator);
                        }
                        String value = null;
                        if (InputColumnType.BRCD.equals(inputColumnType)) {
                            value = data.getBrcd();
                        } else if (InputColumnType.CDN.equals(inputColumnType)) {
                            value = data.getCdn();
                        } else if (InputColumnType.BDGT_TSTM_USE_HMS.equals(inputColumnType)) {
                            value = data.getBdgtTstmUseHms();
                        } else if (InputColumnType.AMSL_AMT.equals(inputColumnType)) {
//                            value = String.valueOf(data.getAmslAmt());
                        } else if (InputColumnType.AFST_NM.equals(inputColumnType)) {
                            value = data.getAfstNm();
                        } else if (InputColumnType.TPBS_NM.equals(inputColumnType)) {
                            value = data.getTpbsNm();
                        } else if (InputColumnType.BZDY_YN.equals(inputColumnType)) {
                            value = data.getBzdyYn();
                        } else if (InputColumnType.AFST_DTL_ADR.equals(inputColumnType)) {
                            value = data.getAfstDtlAdr();
                        } else if (InputColumnType.BRNC_ADR.equals(inputColumnType)) {
                            value = data.getBrncAdr();
                        } else if (InputColumnType.AFST_BZN.equals(inputColumnType)) {
                            value = data.getAfstBzn();
                        } else if (InputColumnType.AMSL_AFST_NO.equals(inputColumnType)) {
                            value = data.getAmslAfstNo();
                        } else if (InputColumnType.AFST_TPBCD.equals(inputColumnType)) {
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
                        OutputColumnType outputColumnType = OutputColumnType.valueOf(info.getColName());
                        if (OutputColumnType.BDMN_ITEX_MNGM_NO.equals(outputColumnType)) {
                            value = data.getBdmnItexMngmNo();
                        } else if (OutputColumnType.BDGT_PRFR_RSN_FRCS_CON.equals(outputColumnType)) {
                            value = data.getBdgtPrfrRsnFrcsCon();
                        } else if (OutputColumnType.BDGT_BSNS_FRCS_CON.equals(outputColumnType)) {
                            value = data.getBrcd() + "-" + data.getBdgtBsnsFrcsCon();
                        }
                        body.append(value);
                    }
                    first = false;
                }
                outputFirst = true;
                body.append("\n");
            }

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

            //mccService.trainModel(form.getId());

            map.put("status", "SUCCESS");
        } catch (Exception e) {
            map.put("status", "FAIL");
        }
        return map;
    }

    public List<LearningModelInfo> getList(LearningModelForm params) {
        List<LearningModelInfo> modelList = Collections.emptyList();

        if (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate())) {
            params.setSearchStartDate(String.valueOf(LocalDate.now().minusMonths(1)).replaceAll("-", "."));
            params.setSearchEndDate(String.valueOf(LocalDate.now()).replaceAll("-", "."));
        }

        int totalCount = learningModelRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);

        params.setPaginationInfo(paginationInfo);

        if (totalCount > 0) {
            if (ObjectUtils.isEmpty(params.getSorting())) {
                params.setSorting("desc");
            }
            modelList = learningModelRepository.getList(params);
        }

        return modelList;
    }

    public LearningModelInfo getLoad(LearningModelForm form) {
        //model
        LearningModelInfo info = learningModelRepository.getLoad(form.getId());

        //model input list set
        info.setInputList(learningModelInputRepository.getPartList(form.getId(), InOutGbnType.INPUT.name()));
        info.setOutputList(learningModelInputRepository.getPartList(form.getId(), InOutGbnType.OUTPUT.name()));
        return info;
    }
}

package kr.co.ibk.service;

import kr.co.ibk.domain.enums.InOutGbnType;
import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.domain.web.TemplateInfo;
import kr.co.ibk.domain.web.TemplateInputInfo;
import kr.co.ibk.model.LearningDataForm;
import kr.co.ibk.model.LearningModelForm;
import kr.co.ibk.model.LearningSchedulerForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningSchedulerService extends _BaseService {

    private final LearningSchedulerRepository learningSchedulerRepository;
    private final TemplateRepository templateRepository;
    private final TemplateInputRepository templateInputRepository;
    private final LearningDataRepository learningDataRepository;
    private final LearningDataInputRepository learningDataInputRepository;
    private final LearningModelRepository learningModelRepository;
    private final LearningModelInputRepository learningModelInputRepository;

    public List<LearningSchedulerInfo> getPage(LearningSchedulerForm params) {
        int totalCount = learningSchedulerRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);
        params.setPaginationInfo(paginationInfo);

        List<LearningSchedulerInfo> list = new ArrayList<>();
        if (totalCount > 0) {
            if (ObjectUtils.isEmpty(params.getSorting())) {
                params.setSorting("desc");
            }
            params.setPagingAt("Y");
            list = getList(params);
        }

        return list;
    }

    public List<LearningSchedulerInfo> getList(LearningSchedulerForm params) {
        return learningSchedulerRepository.getList(params);
    }

    public LearningSchedulerInfo getDetail(Integer schedId) {
        // TODO : 학습 데이터 id 매핑
        return learningSchedulerRepository.getDetail(schedId);
    }

    @Transactional
    public HashMap<String, Object> save(LearningSchedulerForm form, MemberInfo memberInfo) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "FAIL");

        Long saveCnt = 0L;
        form.setModId(memberInfo.getMemId());
        form.setRegId(memberInfo.getMemId());

        if (ObjectUtils.isEmpty(form.getSchedId())) {
            learningSchedulerRepository.setInsert(form);
        } else {
            learningSchedulerRepository.setUpdate(form);
        }

        // 템플릿 조회
        TemplateInfo templateInfo = templateRepository.getLoad(form.getTemplateId());
        List<Map<String, Object>> inputArr = convertToMap(templateInputRepository.getPartList(form.getTemplateId(), InOutGbnType.INPUT.name()));
        List<Map<String, Object>> outputArr = convertToMap(templateInputRepository.getPartList(form.getTemplateId(), InOutGbnType.OUTPUT.name()));

        // TODO : 학습 데이터 id 매핑
        // 학습 데이터 복사
        LearningDataForm dataForm = new LearningDataForm();
        dataForm.setDataName("[배치]" + form.getSchedNm());
        dataForm.setMemId(memberInfo.getMemId());
        dataForm.setLearningType(templateInfo.getLearningType());
        dataForm.setSelectCon(templateInfo.getSelectCon());
        dataForm.setHdqrBobDcd(templateInfo.getHdqrBobDcd());
        dataForm.setTemplateId(form.getTemplateId());
        dataForm.setStartDt(LocalDateTime.now().minusMonths(form.getBdgtPrfrYm()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dataForm.setEndDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        learningDataRepository.insert(dataForm);
        saveCnt = learningDataInputRepository.insertList(dataForm.getId(), inputArr, InOutGbnType.INPUT.name());
        saveCnt += learningDataInputRepository.insertList(dataForm.getId(), outputArr, InOutGbnType.OUTPUT.name());

        // 모델 복사
        LearningModelForm modelForm = new LearningModelForm();
        modelForm.setDataId(dataForm.getId());
        modelForm.setModId(memberInfo.getMemId());
        modelForm.setRegId(memberInfo.getMemId());
        modelForm.setLearnName("[배치]" + form.getSchedNm());
        modelForm.setBatchSize(form.getBatchSize());
        modelForm.setEpoch(form.getEpoch());
        modelForm.setLearningRate(form.getLearningRate());
        modelForm.setHdqrBobDcd(templateInfo.getHdqrBobDcd());
        modelForm.setLearningType(templateInfo.getLearningType());
        learningModelRepository.insert(modelForm);
        saveCnt += learningModelInputRepository.insertList(modelForm.getId(), inputArr, InOutGbnType.INPUT.name());
        saveCnt += learningModelInputRepository.insertList(modelForm.getId(), outputArr, InOutGbnType.OUTPUT.name());

        if (saveCnt > 0) {
            map.put("status", "SUCCESS");
        }
        return map;
    }

    @Transactional
    public int setUpdate(LearningSchedulerForm params, MemberInfo memberInfo) {
        params.setModId(memberInfo.getMemId());
        return learningSchedulerRepository.setUpdate(params);
    }

    @Transactional
    public HashMap<String, Object> deleteAllById(LearningSchedulerForm form, MemberInfo memberInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "FAIL");

        Long deleteCnt;
        if (!ObjectUtils.isEmpty(form.getIdArr()) && form.getIdArr().length > 0) {
            deleteCnt = learningSchedulerRepository.deleteAllById(form.getIdArr(), memberInfo.getMemId());
            if (deleteCnt > 0) {
                map.put("status", "SUCCESS");
            }
        }
        return map;
    }

    @Transactional
    public int setDelete(LearningSchedulerForm params, MemberInfo memberInfo) {
        params.setModId(memberInfo.getMemId());
        return learningSchedulerRepository.setDelete(params);
    }

    private List<Map<String, Object>> convertToMap(List<TemplateInputInfo> list) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (TemplateInputInfo info : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", info.getColName());
            map.put("sno", info.getSno());
            mapList.add(map);
        }
        return mapList;
    }

    public List<Integer> getBatchList() {
        return learningSchedulerRepository.getBatchList();
    }
}

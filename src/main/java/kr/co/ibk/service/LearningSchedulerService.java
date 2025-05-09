package kr.co.ibk.service;

import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.LearningSchedulerForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            saveCnt += learningSchedulerRepository.setInsert(form);
        } else {
            saveCnt += learningSchedulerRepository.setUpdate(form);
        }

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

    @Transactional
    public List<LearningSchedulerInfo> getBatchList() {
        List<LearningSchedulerInfo> rtnList = new ArrayList<>();
        List<LearningSchedulerInfo> list = learningSchedulerRepository.getBatchList();
        for (LearningSchedulerInfo info : list) {
            try {
                if (info != null) {
                    LocalDate dd = LocalDate.parse(info.getStYmd(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate now = LocalDate.now();
                    while (dd.equals(now) || dd.isBefore(now)) {
                        if (dd.equals(now)) {
                            rtnList.add(info);
                            learningSchedulerRepository.updateRunCnt(info.getSchedId());
                            learningSchedulerRepository.updateLatestRunDt(info.getSchedId());
                            break;
                        }
                        dd = dd.plusWeeks(info.getTermTy().getWeek());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rtnList;
    }
}

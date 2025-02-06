package kr.co.ibk.service;

import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.LearningSchedulerForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.LearningSchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningSchedulerService extends _BaseService {

    private final LearningSchedulerRepository learningSchedulerRepository;

    public List<LearningSchedulerInfo> getPage(LearningSchedulerForm params) {
        int totalCount = learningSchedulerRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);
        params.setPaginationInfo(paginationInfo);

        List<LearningSchedulerInfo> list = new ArrayList<>();
        if (totalCount > 0) {

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

    public HashMap<String, Object> save(LearningSchedulerForm params, MemberInfo memberInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "FAIL");

        Long saveCnt = 0L;
        params.setModId(memberInfo.getMemId());
        params.setRegId(memberInfo.getMemId());

        if (ObjectUtils.isEmpty(params.getSchedId())) {
            saveCnt += learningSchedulerRepository.setInsert(params);
        } else {
            learningSchedulerRepository.setUpdate(params);
        }

        if (saveCnt > 0) {
            map.put("status", "SUCCESS");
        }
        return map;
    }

    public int setUpdate(LearningSchedulerForm params, MemberInfo memberInfo) {
        params.setModId(memberInfo.getMemId());
        return learningSchedulerRepository.setUpdate(params);
    }

    public int setDelete(LearningSchedulerForm params, MemberInfo memberInfo) {
        params.setModId(memberInfo.getMemId());
        return learningSchedulerRepository.setDelete(params);
    }
}

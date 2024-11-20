package kr.co.ibk.service;

import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.LearningSchedulerForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.LearningSchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public int setInsert(LearningSchedulerForm params, MemberInfo memberInfo) {
        params.setRegId(memberInfo.getMemId());
        params.setModId(memberInfo.getMemId());
        return learningSchedulerRepository.setInsert(params);
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

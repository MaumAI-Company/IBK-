package kr.co.ibk.repository;

import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.model.LearningSchedulerForm;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningSchedulerRepository {
    int getTotalCount(LearningSchedulerForm params);

    List<LearningSchedulerInfo> getList(LearningSchedulerForm params);

    LearningSchedulerInfo getDetail(@Param("id") Integer id);

    int setInsert(LearningSchedulerForm params);

    int setUpdate(LearningSchedulerForm params);

    int setDelete(LearningSchedulerForm params);

    int countByInTemplateId(Integer[] idArr);

    Long deleteAllById(Integer[] idArr, @Param("memId") String memId);

    List<Integer> getBatchList();
}

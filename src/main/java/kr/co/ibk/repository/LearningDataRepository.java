package kr.co.ibk.repository;

import kr.co.ibk.domain.web.LearningDataInfo;
import kr.co.ibk.model.LearningDataForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningDataRepository {
    void insert(LearningDataForm form);

    void update(LearningDataForm form);

    void updateNullAllByTemplateId(Integer[] idArr);

    int getTotalCount(LearningDataForm params);

    List<LearningDataInfo> getList(LearningDataForm form);

    int learnDataNmCount(@Param("dataName") String dataName);

    LearningDataInfo getLoad(Integer id);

    Long deleteAllById(Integer[] idArr);

    void updateNullAllBySchedId(@Param("schedId") Integer schedId);
}

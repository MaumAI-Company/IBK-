package kr.co.ibk.repository;

import kr.co.ibk.domain.enums.LearningType;
import kr.co.ibk.domain.web.LearningModelInfo;
import kr.co.ibk.model.LearningModelForm;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningModelRepository {

    public LearningModelInfo getLoad(@Param("id") Integer id);

    public void insert(LearningModelForm form);

    void updateFile(LearningModelForm form);

    void updateStatus(LearningModelForm form);

    List<LearningModelInfo> getList(LearningModelForm form);

    int getTotalCount(LearningModelForm form);

    void update(LearningModelForm form);

    int countByInLearningId(Integer[] idArr);

    int modelNmCount(@Param("learnName") String learnName);

    Long deleteAllById(Integer[] idArr, @Param("memId") String memId);

    void updateNullAllByLearningId(Integer[] idArr);

    int countByInIDAndDeployArr(Integer[] idArr, Integer[] deployArr);

    List<LearningModelInfo> getTargetList(@Param("learningType") LearningType learningType, @Param("hdqrBobDcd") Integer hdqrBobDcd, Integer[] deployArr);
}

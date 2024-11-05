package kr.co.ibk.repository;

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
}

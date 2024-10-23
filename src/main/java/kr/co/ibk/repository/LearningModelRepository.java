package kr.co.ibk.repository;

import kr.co.ibk.domain.web.LearningModelInfo;
import kr.co.ibk.model.LearningModelForm;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningModelRepository {
    public LearningModelInfo register(LearningModelForm form);
}

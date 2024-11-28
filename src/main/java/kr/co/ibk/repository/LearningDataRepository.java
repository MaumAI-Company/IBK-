package kr.co.ibk.repository;

import kr.co.ibk.model.LearningDataForm;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningDataRepository {
    void insert(LearningDataForm form);

    void update(LearningDataForm form);

    void updateNullAllByTemplateId(Integer[] idArr);
}

package kr.co.ibk.repository;

import kr.co.ibk.domain.web.CardLearningDataInfo;
import kr.co.ibk.model.SearchForm;
import kr.co.ibk.model.CardLearningDataForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardLearningDataRepository {
    List<CardLearningDataInfo> getList(CardLearningDataForm form);
    List<CardLearningDataInfo> getLearningList(SearchForm form);
    int getTotalCount(CardLearningDataForm form);
}

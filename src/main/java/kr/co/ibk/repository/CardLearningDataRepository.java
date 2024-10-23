package kr.co.ibk.repository;

import kr.co.ibk.domain.web.CardLearningDataInfo;
import kr.co.ibk.model.SearchForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardLearningDataRepository {
    List<CardLearningDataInfo> getList(SearchForm form);
}

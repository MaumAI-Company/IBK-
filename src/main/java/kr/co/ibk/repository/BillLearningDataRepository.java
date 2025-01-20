package kr.co.ibk.repository;

import kr.co.ibk.domain.web.BillLearningDataInfo;
import kr.co.ibk.model.BillLearningDataForm;
import kr.co.ibk.model.SearchForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillLearningDataRepository {
    List<BillLearningDataInfo> getList(BillLearningDataForm form);

    List<BillLearningDataInfo> getLearningList(SearchForm form);

    int getTotalCount(BillLearningDataForm form);
}

package kr.co.ibk.service;

import kr.co.ibk.domain.web.CardLearningDataInfo;
import kr.co.ibk.model.CardLearningDataForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.CardLearningDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardLearningDataService extends _BaseService {
    private final CardLearningDataRepository cardLearningDataRepository;

    /**
     * return list & pagination info
     *
     * @param params
     * @return
     */
    public List<CardLearningDataInfo> page(CardLearningDataForm params) {
        /*
        default search condition
         */
        if (ObjectUtils.isEmpty(params.getSearchTarget())) {
            params.setSearchTarget("1");
        }

        if (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate())) {
            params.setSearchStartDate(String.valueOf(LocalDate.now().minusYears(2)).substring(0, 10));
            params.setSearchEndDate(String.valueOf(LocalDate.now()).substring(0, 10));
        }
        params.setSearchStartDate(params.getSearchStartDate().replaceAll("-", ""));
        params.setSearchEndDate(params.getSearchEndDate().replaceAll("-", ""));

        int totalCount = cardLearningDataRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);
        params.setPaginationInfo(paginationInfo);

        List<CardLearningDataInfo> list = new ArrayList<>();
        if (totalCount > 0) {
            if (ObjectUtils.isEmpty(params.getSorting())) {
                params.setSorting("desc");
            }

            params.setPagingAt("Y");
            list = getList(params);
        }

        return list;
    }

    /**
     * return all list
     *
     * @param form
     * @return
     */
    public List<CardLearningDataInfo> getList(CardLearningDataForm form) {
        return cardLearningDataRepository.getList(form);
    }
}

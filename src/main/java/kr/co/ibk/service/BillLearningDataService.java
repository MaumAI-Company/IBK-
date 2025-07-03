package kr.co.ibk.service;

import kr.co.ibk.domain.web.BillLearningDataInfo;
import kr.co.ibk.model.BillLearningDataForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.BillLearningDataRepository;
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
public class BillLearningDataService extends _BaseService {
    private final BillLearningDataRepository billLearningDataRepository;

    /**
     * return list & pagination info
     *
     * @param params
     * @return
     */
    public List<BillLearningDataInfo> page(BillLearningDataForm params) {
        /*
        default search condition
         */

        if (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate())) {
            params.setSearchStartDate(String.valueOf(LocalDate.now().minusYears(2)).substring(0, 10));
            params.setSearchEndDate(String.valueOf(LocalDate.now()).substring(0, 10));
        }
        params.setSearchStartDate(params.getSearchStartDate().replaceAll("-", ""));
        params.setSearchEndDate(params.getSearchEndDate().replaceAll("-", ""));

        int totalCount = billLearningDataRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);
        params.setPaginationInfo(paginationInfo);

        List<BillLearningDataInfo> list = new ArrayList<>();
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
    public List<BillLearningDataInfo> getList(BillLearningDataForm form) {

        // 통합인 경우 target 검색조건 제거
        if (!ObjectUtils.isEmpty(form.getSearchTarget()) && form.getSearchTarget().equals("3")) {
            form.setSearchTarget(null);
        }

        return billLearningDataRepository.getList(form);
    }

    public int getTotalCount(BillLearningDataForm form) {
        return billLearningDataRepository.getTotalCount(form);
    }
}

package kr.co.ibk.service;

import kr.co.ibk.domain.web.CardLearningDataInfo;
import kr.co.ibk.model.CardLearningDataForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.CardLearningDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

package kr.co.ibk.service;

import kr.co.ibk.domain.web.CardLearningDataInfo;
import kr.co.ibk.model.CardLearningDataForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.CardLearningDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardLearningDataService extends _BaseService {
    private final CardLearningDataRepository cardLearningDataRepository;

    public List<CardLearningDataInfo> getList(CardLearningDataForm form) {

        int totalCount = cardLearningDataRepository.getTotalCount(form);

        PaginationInfo paginationInfo = new PaginationInfo(form);
        paginationInfo.setTotalRecordCount(totalCount);
        form.setPaginationInfo(paginationInfo);

        List<CardLearningDataInfo> list = new ArrayList<>();
        if (totalCount > 0) {
            list = cardLearningDataRepository.getList(form);
        }

        return list;
    }
}

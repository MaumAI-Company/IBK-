package kr.co.ibk.service;

import kr.co.ibk.domain.web.CardLearningDataInfo;
import kr.co.ibk.model.SearchForm;
import kr.co.ibk.repository.CardLearningDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardLearningDataService extends _BaseService {
    private final CardLearningDataRepository cardLearningDataRepository;

    public List<CardLearningDataInfo> getList(SearchForm form) {
        return cardLearningDataRepository.getList(form);
    }
}

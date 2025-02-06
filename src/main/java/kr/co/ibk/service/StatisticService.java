package kr.co.ibk.service;

import kr.co.ibk.domain.enums.LearningType;
import kr.co.ibk.domain.web.StatisticInfo;
import kr.co.ibk.model.StatisticInfoForm;
import kr.co.ibk.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticService extends _BaseService {

    private final StatisticRepository statisticRepository;

    public List<StatisticInfo> getStatisticInput(StatisticInfoForm form) {
        if (ObjectUtils.isEmpty(form.getSearchStartDate()) || ObjectUtils.isEmpty(form.getSearchEndDate())) {
            form.setSearchStartDate(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
            form.setSearchEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        }

        List<StatisticInfo> infos;
        LearningType learningType = form.getSearchLearningType() != null ? form.getSearchLearningType() : LearningType.CARD;

        if (learningType.equals(LearningType.CARD)) {
            infos = statisticRepository.getCardInput(form);
        } else {
            form.setSearchStartDate(trimLastTwoChars(form.getSearchStartDate()));
            form.setSearchEndDate(trimLastTwoChars(form.getSearchEndDate()));
            infos = statisticRepository.getBillInput(form);
        }
        return infos;
    }

    public List<StatisticInfo> getStatisticOutput(StatisticInfoForm form) {
        if (ObjectUtils.isEmpty(form.getSearchStartDate()) || ObjectUtils.isEmpty(form.getSearchEndDate())) {
            form.setSearchStartDate(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
            form.setSearchEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        }

        List<StatisticInfo> infos;
        LearningType learningType = form.getSearchLearningType() != null ? form.getSearchLearningType() : LearningType.CARD;

        if (learningType.equals(LearningType.CARD)) {
            infos = statisticRepository.getCardOutput(form);
        } else {
            form.setSearchStartDate(trimLastTwoChars(form.getSearchStartDate().replaceAll("/.", "")));
            form.setSearchEndDate(trimLastTwoChars(form.getSearchEndDate()));
            infos = statisticRepository.getBillOutput(form);
        }
        return infos;
    }

    private String trimLastTwoChars(String value) {
        if (value != null && value.length() > 2) {
            return value.substring(0, value.length() - 2);
        }
        return value;
    }
}

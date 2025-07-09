package kr.co.ibk.service;

import kr.co.ibk.common.utils.NullHelper;
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

    public List<StatisticInfo> inferResultStatistic(StatisticInfoForm form) {
        if (ObjectUtils.isEmpty(form.getSearchStartDate()) || ObjectUtils.isEmpty(form.getSearchEndDate())) {
            form.setSearchStartDate(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
            form.setSearchEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        }

        List<StatisticInfo> infos;
        LearningType learningType = !ObjectUtils.isEmpty(form.getSearchLearningType()) ? LearningType.valueOf(form.getSearchLearningType()) : null;

        if (NullHelper.isNull(learningType)) { // 전체
            infos = statisticRepository.totalInferResultStatistic(form);
        } else if (learningType.equals(LearningType.CARD)) {
            infos = statisticRepository.cardInferResultStatistic(form);
        } else {
            infos = statisticRepository.billInferResultStatistic(form);
        }
        return infos;
    }

    public List<StatisticInfo> usageStatistic(StatisticInfoForm form) {
        List<StatisticInfo> infos;
        if (ObjectUtils.isEmpty(form.getSearchStartDate()) || ObjectUtils.isEmpty(form.getSearchEndDate())) {
            form.setSearchStartDate(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
            form.setSearchEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        }
        LearningType learningType = !ObjectUtils.isEmpty(form.getSearchLearningType()) ? LearningType.valueOf(form.getSearchLearningType()) : null;

        if (NullHelper.isNull(learningType)) { // 전체
            infos = statisticRepository.totalUsageStatistic(form);
        } else if (learningType.equals(LearningType.CARD)) {
            infos = statisticRepository.cardUsageStatistic(form);
        } else {
            infos = statisticRepository.billUsageStatistic(form);
        }
        return infos;
    }
}

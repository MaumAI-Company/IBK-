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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticService extends _BaseService {

    private final StatisticRepository statisticRepository;

    public List<StatisticInfo> inferResultStatistic(StatisticInfoForm form) {
        // 날짜검색 (default 당월 1일~말일)
        if (ObjectUtils.isEmpty(form.getSearchStartDate()) || ObjectUtils.isEmpty(form.getSearchEndDate())) {
            form.setSearchStartDate(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            form.setSearchEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        } else {
            // 기존 입력된 날짜에서 '.' 제거 (yyyy.MM.dd → yyyyMMdd)
            form.setSearchStartDate(form.getSearchStartDate().replace(".", ""));
            form.setSearchEndDate(form.getSearchEndDate().replace(".", ""));
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
        // 날짜검색 (default 당월 1일~말일)
        if (ObjectUtils.isEmpty(form.getSearchStartDate()) || ObjectUtils.isEmpty(form.getSearchEndDate())) {
            form.setSearchStartDate(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            form.setSearchEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        } else {
            // 기존 입력된 날짜에서 '.' 제거 (yyyy.MM.dd → yyyyMMdd)
            form.setSearchStartDate(form.getSearchStartDate().replace(".", ""));
            form.setSearchEndDate(form.getSearchEndDate().replace(".", ""));
        }

        infos = statisticRepository.usageStatistic(form);
        return infos;
    }

    public List<StatisticInfo> aiPrfrStatistic(StatisticInfoForm form) {
        List<StatisticInfo> infos;

        // 날짜검색 (default 당월 1일~말일)
        if (ObjectUtils.isEmpty(form.getSearchStartDate()) || ObjectUtils.isEmpty(form.getSearchEndDate())) {
            form.setSearchStartDate(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            form.setSearchEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        } else {
            // 기존 입력된 날짜에서 '.' 제거 (yyyy.MM.dd → yyyyMMdd)
            form.setSearchStartDate(form.getSearchStartDate().replace(".", ""));
            form.setSearchEndDate(form.getSearchEndDate().replace(".", ""));
        }
        infos = statisticRepository.aiPrfrStatistic(form);
        return infos;

        /*Map<String, StatisticInfo> map = infos.stream()
                .collect(Collectors.toMap(StatisticInfo::getDd, Function.identity()));

        // 날짜 계산
        LocalDate start = LocalDate.parse(form.getSearchStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate end = LocalDate.parse(form.getSearchEndDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<StatisticInfo> result = new ArrayList<>();
        String cycle = form.getSearchCycle(); // "1" = 일별, "2" = 월별, "3" = 연도별
        DateTimeFormatter formatter;
        ChronoUnit unit;

        switch (cycle) {
            case "2": // 월별
                formatter = DateTimeFormatter.ofPattern("yyyyMM");
                start = start.withDayOfMonth(1); // 월초로 정렬
                end = end.withDayOfMonth(1);
                unit = ChronoUnit.MONTHS;
                break;
            case "3": // 연도별
                formatter = DateTimeFormatter.ofPattern("yyyy");
                start = start.withDayOfYear(1);
                end = end.withDayOfYear(1);
                unit = ChronoUnit.YEARS;
                break;
            case "1":
            default: // 일별
                formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                unit = ChronoUnit.DAYS;
                break;
        }

        for (LocalDate date = start; !date.isAfter(end); date = date.plus(1, unit)) {
            String key = date.format(formatter);
            result.add(map.getOrDefault(key, new StatisticInfo(key)));
        }

        return result;*/
    }
}

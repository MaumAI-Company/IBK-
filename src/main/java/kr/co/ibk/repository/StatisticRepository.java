package kr.co.ibk.repository;

import kr.co.ibk.domain.web.StatisticInfo;
import kr.co.ibk.model.StatisticInfoForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository {
    List<StatisticInfo> cardInferResultStatistic(StatisticInfoForm form);

    List<StatisticInfo> billInferResultStatistic(StatisticInfoForm form);

    List<StatisticInfo> totalInferResultStatistic(StatisticInfoForm form);

    List<StatisticInfo> cardUsageStatistic(StatisticInfoForm form);

    List<StatisticInfo> billUsageStatistic(StatisticInfoForm form);

    List<StatisticInfo> totalUsageStatistic(StatisticInfoForm form);

}

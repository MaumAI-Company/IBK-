package kr.co.ibk.repository;

import kr.co.ibk.domain.enums.StatisticTargetType;
import kr.co.ibk.domain.web.AiPrfrStatInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiPrfrStatRepository {
    List<AiPrfrStatInfo> getCardStatisticByAll();

    List<AiPrfrStatInfo> getBillStatisticByAll();

    List<AiPrfrStatInfo> getCardStatisticByRange();

    List<AiPrfrStatInfo> getBillStatisticByRange();

    int countAiPrfrStat(AiPrfrStatInfo info);

    void update(AiPrfrStatInfo info);

    void insert(AiPrfrStatInfo info);

    int deleteByType(StatisticTargetType type);
}
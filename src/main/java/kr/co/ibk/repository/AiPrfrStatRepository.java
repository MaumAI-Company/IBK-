package kr.co.ibk.repository;

import kr.co.ibk.domain.web.AiPrfrStatInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiPrfrStatRepository {
    List<AiPrfrStatInfo> getCardStatistic();

    List<AiPrfrStatInfo> getBillStatistic();

    int existsAiPrfrStat(AiPrfrStatInfo info);

    void updateAiPrfrStat(AiPrfrStatInfo info);

    void insertAiPrfrStat(AiPrfrStatInfo info);
}
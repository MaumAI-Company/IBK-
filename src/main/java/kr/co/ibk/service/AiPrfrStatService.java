package kr.co.ibk.service;

import kr.co.ibk.domain.enums.StatisticTargetType;
import kr.co.ibk.domain.web.AiPrfrStatInfo;
import kr.co.ibk.repository.AiPrfrStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiPrfrStatService extends _BaseService {

    private final AiPrfrStatRepository aiPrfrStatRepository;

    @Transactional
    public void updateStatistic() {
        // BC 카드
        List<AiPrfrStatInfo> cardStats = aiPrfrStatRepository.getCardStatistic();
        for (AiPrfrStatInfo stat : cardStats) {
            stat.setType(StatisticTargetType.CARD);
            if (aiPrfrStatRepository.existsAiPrfrStat(stat) > 0) {
                aiPrfrStatRepository.updateAiPrfrStat(stat);
            } else {
                aiPrfrStatRepository.insertAiPrfrStat(stat);
            }
        }
        log.info("[AI 사용 지급결의 사용 개수] updateStatistic 실행 - 카드 업데이트된 ROW 수: {}", cardStats.size());

        // 세금계산서
        List<AiPrfrStatInfo> billStats = aiPrfrStatRepository.getBillStatistic();
        for (AiPrfrStatInfo stat : billStats) {
            stat.setType(StatisticTargetType.BILL);
            if (aiPrfrStatRepository.existsAiPrfrStat(stat) > 0) {
                aiPrfrStatRepository.updateAiPrfrStat(stat);
            } else {
                aiPrfrStatRepository.insertAiPrfrStat(stat);
            }
        }
        log.info("[AI 사용 지급결의 사용 개수] updateStatistic 실행 - 세금계산서 업데이트된 ROW 수: {}", billStats.size());
    }

}

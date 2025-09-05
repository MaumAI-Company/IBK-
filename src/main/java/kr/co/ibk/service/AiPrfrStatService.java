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

    /**
     * AI 사용 지급결의 사용 개수 통계를 대상 타입별로 전체 재생성
     * - targetType == null 이면 모든 타입(CARD, BILL)을 순차 재생성
     * - 각 타입별 전체 집계 후 삭제/저장
     *
     * @param targetType 재생성 대상 타입 (null: 전 타입, CARD, BILL)
     */
    @Transactional
    public void updateStatisticByAll(StatisticTargetType targetType) {
        // BC카드
        if (targetType == null || targetType == StatisticTargetType.CARD) {
            List<AiPrfrStatInfo> stats = aiPrfrStatRepository.getCardStatisticByAll();
            this.replaceAllUserUsageStat(StatisticTargetType.CARD, stats);
        }

        // 세금계산서
        if (targetType == null || targetType == StatisticTargetType.BILL) {
            List<AiPrfrStatInfo> stats = aiPrfrStatRepository.getBillStatisticByAll();
            this.replaceAllUserUsageStat(StatisticTargetType.BILL, stats);
        }
    }

    /**
     * 주어진 타입의 기존 통계를 모두 삭제 & 일괄 삽입
     *
     * @param type 통계 타입
     * @param stats 새로 삽입할 통계 목록
     */
    private void replaceAllUserUsageStat(StatisticTargetType type, List<AiPrfrStatInfo> stats) {
        // 삭제
        int deleteCnt = aiPrfrStatRepository.deleteByType(type);
        log.info("[AI사용지급결의사용개수] updateStatisticByAll 실행 - {} 삭제된 ROW 수: {}", type.getName(), deleteCnt);

        // 등록
        int inserted = 0;
        for (AiPrfrStatInfo s : stats) {
            s.setType(type);
            aiPrfrStatRepository.insert(s);
            inserted++;
        }
        log.info("[AI사용지급결의사용개수] updateStatisticByAll 실행 - {} 등록된 ROW 수: {}", type.getName(), inserted);
    }

    @Transactional
    public void updateStatisticByRange() {
        // BC 카드
        List<AiPrfrStatInfo> cardStats = aiPrfrStatRepository.getCardStatisticByRange();
        for (AiPrfrStatInfo stat : cardStats) {
            stat.setType(StatisticTargetType.CARD);
            if (aiPrfrStatRepository.countAiPrfrStat(stat) > 0) { // 존재하면 update, 없으면 insert
                aiPrfrStatRepository.update(stat);
            } else {
                aiPrfrStatRepository.insert(stat);
            }
        }
        log.info("[AI 사용 지급결의 사용 개수] updateStatisticByRange 실행 - 카드 업데이트된 ROW 수: {}", cardStats.size());

        // 세금계산서
        List<AiPrfrStatInfo> billStats = aiPrfrStatRepository.getBillStatisticByRange();
        for (AiPrfrStatInfo stat : billStats) {
            stat.setType(StatisticTargetType.BILL);
            if (aiPrfrStatRepository.countAiPrfrStat(stat) > 0) { // 존재하면 update, 없으면 insert
                aiPrfrStatRepository.update(stat);
            } else {
                aiPrfrStatRepository.insert(stat);
            }
        }
        log.info("[AI 사용 지급결의 사용 개수] updateStatisticByRange 실행 - 세금계산서 업데이트된 ROW 수: {}", billStats.size());
    }

}

package kr.co.ibk.service;

import kr.co.ibk.domain.enums.StatisticTargetType;
import kr.co.ibk.domain.web.AiPrfrStatInfo;
import kr.co.ibk.repository.AiPrfrStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        log.info("[AI사용지급결의사용개수] 기간 통계 배치 시작");

        // 신규 통계 조회
        List<AiPrfrStatInfo> newCardStats = aiPrfrStatRepository.getCardStatisticByRange();
        List<AiPrfrStatInfo> newBillStats = aiPrfrStatRepository.getBillStatisticByRange();

        // 삭제 통합 처리 (기존에 있는데 없어진 경우)
        Set<AiPrfrStatInfo> newAllKeySet = Stream.concat(newCardStats.stream(), newBillStats.stream())
                .map(stat -> {
                    AiPrfrStatInfo key = new AiPrfrStatInfo();
                    key.setType(stat.getType());
                    key.setBdgtPrfrYmd(stat.getBdgtPrfrYmd());
                    key.setHdqrBobDcd(stat.getHdqrBobDcd());
                    return key;
                }).collect(Collectors.toSet());

        // 기존 통계 조회에서 없는 키 추출
        List<AiPrfrStatInfo> existingStats = aiPrfrStatRepository.getExistingStatsByRange();
        List<Long> statIdsToDelete = existingStats.stream()
                .filter(existing -> !newAllKeySet.contains(existing))
                .map(AiPrfrStatInfo::getStatId)
                .collect(Collectors.toList());

        if (!statIdsToDelete.isEmpty()) { // 삭제할 PK 있는 경우
            aiPrfrStatRepository.deleteByIds(statIdsToDelete);
            log.info("[AI사용지급결의사용개수] 삭제 완료 - 총 {}건", statIdsToDelete.size());
        }

        // BC카드 등록/수정
        for (AiPrfrStatInfo stat : newCardStats) {
            stat.setType(StatisticTargetType.CARD);
            if (aiPrfrStatRepository.countAiPrfrStat(stat) > 0) {
                aiPrfrStatRepository.update(stat);
            } else {
                aiPrfrStatRepository.insert(stat);
            }
        }
        log.info("[AI사용지급결의사용개수] updateStatisticByRange 실행 - 카드 업데이트된 ROW 수: {}", newCardStats.size());

        // 세금계산서 등록/수정
        for (AiPrfrStatInfo stat : newBillStats) {
            stat.setType(StatisticTargetType.BILL);
            if (aiPrfrStatRepository.countAiPrfrStat(stat) > 0) {
                aiPrfrStatRepository.update(stat);
            } else {
                aiPrfrStatRepository.insert(stat);
            }
        }
        log.info("[AI사용지급결의사용개수] updateStatisticByRange 실행 - 세금계산서 업데이트된 ROW 수: {}", newCardStats.size());

    }

}

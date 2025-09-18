package kr.co.ibk.service;

import kr.co.ibk.domain.enums.StatisticTargetType;
import kr.co.ibk.domain.web.UserStatusStatInfo;
import kr.co.ibk.repository.UserUsageStatRepository;
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
public class UserUsageStatService extends _BaseService {

    private final UserUsageStatRepository usageStatRepository;

    /**
     * 사용자활용현황 통계를 대상 타입별로 전체 재생성
     * - targetType == null 이면 모든 타입(CARD, BILL)을 순차 재생성
     * - 각 타입별 전체 집계 후 삭제/저장
     *
     * @param targetType 재생성 대상 타입 (null: 전 타입, CARD, BILL)
     */
    @Transactional
    public void updateStatisticByAll(StatisticTargetType targetType) {
        // BC카드
        if (targetType == null || targetType == StatisticTargetType.CARD) {
            List<UserStatusStatInfo> stats = usageStatRepository.getCardStatisticByAll();
            this.replaceAllUserUsageStat(StatisticTargetType.CARD, stats);
        }

        // 세금계산서
        if (targetType == null || targetType == StatisticTargetType.BILL) {
            List<UserStatusStatInfo> stats = usageStatRepository.getBillStatisticByAll();
            this.replaceAllUserUsageStat(StatisticTargetType.BILL, stats);
        }
    }

    /**
     * 주어진 타입의 기존 통계를 모두 삭제 & 일괄 삽입
     *
     * @param type 통계 타입
     * @param stats 새로 삽입할 통계 목록
     */
    private void replaceAllUserUsageStat(StatisticTargetType type, List<UserStatusStatInfo> stats) {
        // 삭제
        int deleteCnt = usageStatRepository.deleteByType(type);
        log.info("[사용자활용현황] updateStatisticByAll 실행 - {} 삭제된 ROW 수: {}", type.getName(), deleteCnt);

        // 등록
        int inserted = 0;
        for (UserStatusStatInfo s : stats) {
            s.setType(type);
            usageStatRepository.insert(s);
            inserted++;
        }
        log.info("[사용자활용현황] updateStatisticByAll 실행 - {} 등록된 ROW 수: {}", type.getName(), inserted);
    }

    @Transactional
    public void updateStatisticByRange() {
        log.info("[사용자활용현황] 기간 통계 배치 시작");

        // 신규 통계 조회
        List<UserStatusStatInfo> newCardStats = usageStatRepository.getCardStatisticByRange();
        List<UserStatusStatInfo> newBillStats = usageStatRepository.getBillStatisticByRange();

        // 삭제 통합 처리 (기존에 있는데 없어진 경우)
        Set<UserStatusStatInfo> newAllKeySet = Stream.concat(newCardStats.stream(), newBillStats.stream())
                .map(stat -> {
                    UserStatusStatInfo key = new UserStatusStatInfo();
                    key.setType(stat.getType());
                    key.setRsreYmd(stat.getRsreYmd());
                    key.setHdqrBobDcd(stat.getHdqrBobDcd());
                    return key;
                }).collect(Collectors.toSet());

        // 기존 통계 조회에서 없는 키 추출
        List<UserStatusStatInfo> existingStats = usageStatRepository.getExistingStatsByRange();
        List<Long> statIdsToDelete = existingStats.stream()
                .filter(existing -> !newAllKeySet.contains(existing))
                .map(UserStatusStatInfo::getStatId)
                .collect(Collectors.toList());
        if (!statIdsToDelete.isEmpty()) { // 삭제할 PK 있는 경우
            usageStatRepository.deleteByIds(statIdsToDelete);
            log.info("[사용자활용현황] 삭제 완료 - 총 {}건", statIdsToDelete.size());
        }

        // BC카드 등록/수정
        for (UserStatusStatInfo stat : newCardStats) {
            stat.setType(StatisticTargetType.CARD);
            if (usageStatRepository.countUserUsageStat(stat) > 0) {
                usageStatRepository.update(stat);
            } else {
                usageStatRepository.insert(stat);
            }
        }
        log.info("[사용자활용현황] updateStatisticByRange 실행 - 카드 업데이트된 ROW 수: {}", newCardStats.size());

        // 세금계산서 등록/수정
        for (UserStatusStatInfo stat : newBillStats) {
            stat.setType(StatisticTargetType.BILL);
            if (usageStatRepository.countUserUsageStat(stat) > 0) {
                usageStatRepository.update(stat);
            } else {
                usageStatRepository.insert(stat);
            }
        }
        log.info("[사용자활용현황] updateStatisticByRange 실행 - 세금계산서 업데이트된 ROW 수: {}", newBillStats.size());
    }

}

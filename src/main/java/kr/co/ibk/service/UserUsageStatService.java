package kr.co.ibk.service;

import kr.co.ibk.domain.enums.StatisticTargetType;
import kr.co.ibk.domain.web.UserStatusStatInfo;
import kr.co.ibk.repository.UserUsageStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserUsageStatService extends _BaseService {

    private final UserUsageStatRepository usageStatRepository;

    @Transactional
    public void updateStatistic() {
        // BC 카드
        List<UserStatusStatInfo> cardStats = usageStatRepository.getCardStatistic();
        for (UserStatusStatInfo stat : cardStats) {
            stat.setType(StatisticTargetType.CARD);
            if (usageStatRepository.existsUserUsageStat(stat) > 0) {
                usageStatRepository.updateUserUsageStat(stat);
            } else {
                usageStatRepository.insertUserUsageStat(stat);
            }
        }
        log.info("[사용자활용현황] updateStatistic 실행 - 카드 업데이트된 ROW 수: {}", cardStats.size());

        // 세금계산서
        List<UserStatusStatInfo> billStats = usageStatRepository.getBillStatistic();
        for (UserStatusStatInfo stat : billStats) {
            stat.setType(StatisticTargetType.BILL);
            if (usageStatRepository.existsUserUsageStat(stat) > 0) {
                usageStatRepository.updateUserUsageStat(stat);
            } else {
                usageStatRepository.insertUserUsageStat(stat);
            }
        }
        log.info("[사용자활용현황] updateStatistic 실행 - 세금계산서 업데이트된 ROW 수: {}", billStats.size());
    }

    @Transactional
    public void updateStatisticByRange() {
        // BC 카드
        List<UserStatusStatInfo> cardStats = usageStatRepository.getCardStatisticByRange();
        for (UserStatusStatInfo stat : cardStats) {
            stat.setType(StatisticTargetType.CARD);
            if (usageStatRepository.existsUserUsageStat(stat) > 0) {
                usageStatRepository.updateUserUsageStat(stat);
            } else {
                usageStatRepository.insertUserUsageStat(stat);
            }
        }
        log.info("[사용자활용현황] updateStatisticByRange 실행 - 카드 업데이트된 ROW 수: {}", cardStats.size());

        // 세금계산서
        List<UserStatusStatInfo> billStats = usageStatRepository.getBillStatisticByRange();
        for (UserStatusStatInfo stat : billStats) {
            stat.setType(StatisticTargetType.BILL);
            if (usageStatRepository.existsUserUsageStat(stat) > 0) {
                usageStatRepository.updateUserUsageStat(stat);
            } else {
                usageStatRepository.insertUserUsageStat(stat);
            }
        }
        log.info("[사용자활용현황] updateStatisticByRange 실행 - 세금계산서 업데이트된 ROW 수: {}", billStats.size());
    }
}

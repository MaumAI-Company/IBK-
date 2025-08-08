package kr.co.ibk.repository;

import kr.co.ibk.domain.web.UserStatusStatInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserUsageStatRepository {
    List<UserStatusStatInfo> getCardStatistic();

    List<UserStatusStatInfo> getBillStatistic();

    int existsUserUsageStat(UserStatusStatInfo info);

    void updateUserUsageStat(UserStatusStatInfo info);

    void insertUserUsageStat(UserStatusStatInfo info);

    List<UserStatusStatInfo> getCardStatisticByRange();

    List<UserStatusStatInfo> getBillStatisticByRange();
}
package kr.co.ibk.repository;

import kr.co.ibk.domain.enums.StatisticTargetType;
import kr.co.ibk.domain.web.UserStatusStatInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserUsageStatRepository {
    List<UserStatusStatInfo> getCardStatisticByAll();

    List<UserStatusStatInfo> getBillStatisticByAll();

    List<UserStatusStatInfo> getCardStatisticByRange();

    List<UserStatusStatInfo> getBillStatisticByRange();

    int countUserUsageStat(UserStatusStatInfo info);

    void update(UserStatusStatInfo info);

    void insert(UserStatusStatInfo info);

    int deleteByType(StatisticTargetType type);
}
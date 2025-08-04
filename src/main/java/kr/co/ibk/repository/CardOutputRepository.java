package kr.co.ibk.repository;

import kr.co.ibk.domain.web.CardOutputInfo;
import kr.co.ibk.domain.web.UserStatusStatInfo;
import kr.co.ibk.model.CardInputForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardOutputRepository {
    CardOutputInfo getDetail(CardInputForm form);

    int updateHitYn();

    List<UserStatusStatInfo> getUserUsageStat();

    int existsUserUsageStat(UserStatusStatInfo cardStats);

    void updateUserUsageStat(UserStatusStatInfo cardStats);

    void insertUserUsageStat(UserStatusStatInfo cardStats);
}
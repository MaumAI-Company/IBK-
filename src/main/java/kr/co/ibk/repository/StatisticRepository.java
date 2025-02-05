package kr.co.ibk.repository;

import kr.co.ibk.domain.web.StatisticInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository {
    List<StatisticInfo> getStatisticInput();

    List<StatisticInfo> getStatisticOutput();
}

package kr.co.ibk.repository;

import kr.co.ibk.domain.web.StatisticInfo;
import kr.co.ibk.model.StatisticInfoForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository {
    List<StatisticInfo> getCardInput(StatisticInfoForm form);

    List<StatisticInfo> getCardOutput(StatisticInfoForm form);

    List<StatisticInfo> getBillInput(StatisticInfoForm form);

    List<StatisticInfo> getBillOutput(StatisticInfoForm form);

}

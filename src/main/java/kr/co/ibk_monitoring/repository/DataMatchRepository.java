package kr.co.ibk_monitoring.repository;

import org.springframework.stereotype.Repository;


@Repository
public interface DataMatchRepository {

    /**
     * 오늘 기준 CARD_INPUT 과 CARD_OUTPUT 건수가 일치하는지 여부 반환
     * @return true = 일치, false = 불일치
     */
    boolean cardCheck();

    /**
     * 오늘 기준 BILL_INPUT 과 BILL_OUTPUT 건수가 일치하는지 여부 반환
     * @return true = 일치, false = 불일치
     */
    boolean billCheck();
}

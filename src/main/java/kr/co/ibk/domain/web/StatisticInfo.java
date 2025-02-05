package kr.co.ibk.domain.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticInfo {

    private String dd;
    private Integer total;

    /* statics input */
    private Double outCnt;
    private Double per;

    /* statics output */
    private Double hitCnt1;
    private Double hitCnt2;
    private Double hitCnt3;
    private Double hitCnt1Per;
    private Double hitCnt2Per;
    private Double hitCnt3Per;

}



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
    private Integer outCnt;
    private Double per;

    /* statics output */
    private Integer hitCnt1;
    private Integer hitCnt2;
    private Integer hitCnt3;
    private Double hitCnt1Per;
    private Double hitCnt2Per;
    private Double hitCnt3Per;

}



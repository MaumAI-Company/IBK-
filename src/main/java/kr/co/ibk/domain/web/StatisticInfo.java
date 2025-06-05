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

    /* InferResult statics (추론 결과 수 톨계)*/
    private Integer outCnt;
    private Double per;

    /* Usage statics (사용자 활용 현황 톨계)*/
    private Integer hitCnt1;
    private Integer hitCnt2;
    private Integer hitCnt3;
    private Integer hitCnt4;
    private Double hitCnt1Per;
    private Double hitCnt2Per;
    private Double hitCnt3Per;
    private Double hitCnt4Per;
    private Integer totalOutCnt;

}



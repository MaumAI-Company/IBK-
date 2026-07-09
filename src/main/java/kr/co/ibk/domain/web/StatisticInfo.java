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
    private Integer arinUseCnt;
    private Double hitCnt1Per;
    private Double hitCnt2Per;
    private Double hitCnt3Per;
    private Double hitCnt4Per;
    private Double arinUsePer;
    private Integer totalOutCnt;

    // dd만 넣고 나머지는 0으로 초기화하는 생성자
    public StatisticInfo(String dd) {
        this.dd = dd;
        this.total = 0;

        this.outCnt = 0;
        this.per = 0.0;

        this.hitCnt1 = 0;
        this.hitCnt2 = 0;
        this.hitCnt3 = 0;
        this.hitCnt4 = 0;
        this.arinUseCnt = 0;

        this.hitCnt1Per = 0.0;
        this.hitCnt2Per = 0.0;
        this.hitCnt3Per = 0.0;
        this.hitCnt4Per = 0.0;
        this.arinUsePer = 0.0;

        this.totalOutCnt = 0;
    }
}



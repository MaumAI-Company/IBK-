package kr.co.ibk.domain.web;

import kr.co.ibk.domain.enums.StatisticTargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusStatInfo {
    private String statId;
    private StatisticTargetType type;
    private String rsreYmd;
    private String rsreYm;
    private String rsreY;
    private String hdqrBobDcd;
    private Integer total;
    private Integer hitCnt1;
    private Integer hitCnt2;
    private Integer hitCnt3;
    private Integer hitCnt4;
    private Integer arinUseCnt;
    private Integer totalOutputCnt;
}



package kr.co.ibk.domain.web;

import kr.co.ibk.domain.enums.StatisticTargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusStatInfo {
    private Long statId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStatusStatInfo)) return false;
        UserStatusStatInfo that = (UserStatusStatInfo) o;
        return Objects.equals(type, that.type)
                && Objects.equals(rsreYmd, that.rsreYmd)
                && Objects.equals(hdqrBobDcd, that.hdqrBobDcd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, rsreYmd, hdqrBobDcd);
    }
}



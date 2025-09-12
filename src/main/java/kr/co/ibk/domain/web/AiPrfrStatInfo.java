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
public class AiPrfrStatInfo {
    private Long statId;
    private StatisticTargetType type;
    private String bdgtPrfrYmd;
    private String bdgtPrfrYm;
    private String bdgtPrfrY;
    private String hdqrBobDcd;
    private Integer total;
    private Integer hitCnt1;
    private Integer hitCnt2;
    private Integer hitCnt3;
    private Integer hitCnt4;
    private Integer arinUseCnt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AiPrfrStatInfo)) return false;
        AiPrfrStatInfo that = (AiPrfrStatInfo) o;
        return type == that.type &&
                Objects.equals(bdgtPrfrYmd, that.bdgtPrfrYmd) &&
                Objects.equals(hdqrBobDcd, that.hdqrBobDcd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, bdgtPrfrYmd, hdqrBobDcd);
    }
}



package kr.co.ibk.domain.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillInputInfo {
    private String issSrn;
    private String brcd;
    private String hdqrBobDcd;
    private String txblDcd;
    private String splrBsnnNo;
    private String splrFrm;
    private String splrBzstNm;
    private String splrItmsNm;
    private BigDecimal issAmt;
    private String txblLsarNm;
    private String rcvYmd;
    private String jobYmd;
    private String jobYn;
    private LocalDateTime frrgTs;
    private String frrgEmn;
    private LocalDateTime lsmdTs;
    private String lsmdEmn;
    private Integer no;

    private String bdgtItexFrcsPrbCon;
    private String bdgtPrfrRsnFrcsCon;
    private String bdgtBsnsFrcsPrbCon;
    private String rsreYmd;

}

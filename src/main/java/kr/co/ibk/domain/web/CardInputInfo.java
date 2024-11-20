package kr.co.ibk.domain.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardInputInfo {
    private String hdqrBobDcd;
    private String cdn;
    private LocalDateTime frrgTs;
    private String bdgtTstmUseHms;
    private Integer amslAmt;
    private String afstNm;
    private String bdgtItexFrcsCon;
    private String bdgtBsnsFrcsCon;
    private String bdgtPrfrRsnFrcsCon;
    private String bzdyYn;
    private String tpbsNm;
    private String afstBzn;
    private String amslAfstNo;
    private String afstTpbcd;
    private String afstDtlAdr;
    private String brncAdr;
    private String rsreYmd;

    private String tstmYmd;
    private String tstmNo;
    private String brcd;
    private Integer no;
}



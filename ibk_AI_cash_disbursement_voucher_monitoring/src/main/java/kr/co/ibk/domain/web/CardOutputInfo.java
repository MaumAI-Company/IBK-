package kr.co.ibk.domain.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardOutputInfo {
    private String bdgtItexFrcsCon;
    private String bdgtBsnsFrcsCon;
    private String bdgtPrfrRsnFrcsCon;
    private Integer learningModelId;

    private String bdgtItexFrcsPrbCon;
    private String bdgtBsnsFrcsPrbCon;
    private String bdgtPrfrRsnFrcsPrbCon;

    private List<LearningModelInputInfo> inputList;
    private List<LearningModelInputInfo> outputList;

    /*input columns*/
    private String brcd;
    private String cdn;
    private String bdgtTstmUseHms;
    private Integer amslAmt;
    private String afstNm;
    private String tpbsNm;
    private String bzdyYn;
    private String afstDtlAdr;
    private String brncAdr;
    private String afstBzn;
    private String amslAfstNo;
    private String afstTpbcd;

    private Integer no;
    private Integer prevNo;
    private Integer nextNo;

    private String learnName;

    private String tstmYmd;
    private String tstmNo;
    private String rsreYmd;
    private String hdqrBobDcd;
}



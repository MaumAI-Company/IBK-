package kr.co.ibk.domain.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillOutputInfo {
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
    private Integer prevNo;
    private Integer nextNo;

    private String learnName;
}



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
}



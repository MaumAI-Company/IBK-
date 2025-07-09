package kr.co.ibk.model;

import kr.co.ibk.domain.enums.LearningType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatisticInfoForm {
    /*검색조건*/
    private String searchStartDate;
    private String searchEndDate;
    private Integer searchTarget;
    private String searchCycle;
    private String searchLearningType;
}



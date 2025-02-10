package kr.co.ibk.model;

import kr.co.ibk.domain.enums.TermType;
import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class LearningSchedulerForm extends PageForm {
    /*검색조건*/
    private String pagingAt;
    private String sorting;
    private String searchLearningType;
    private String searchTarget;
    private String searchKeyword;

    //insert/update
    private String schedNm;
    private String hdqrBobDcd;
    private TermType termTy;
    private LocalDate stYmd;
    private LocalTime stTime;
    private String stDt;
    private String useAt;
    private String regId;
    private String modId;
    private Integer learningModelId;
    private Integer templateId;
    private Integer bdgtPrfrYm;
    private String epoch;
    private String learningRate;
    private String batchSize;

    private Integer schedId;

    /*delete*/
    private Integer[] idArr;
}



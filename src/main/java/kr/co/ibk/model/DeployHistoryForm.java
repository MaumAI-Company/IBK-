package kr.co.ibk.model;

import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeployHistoryForm extends PageForm {
    /*조회기간*/
    private String searchStartDate;
    private String searchEndDate;

    /*검색조건*/
    private String pagingAt;
    private String sorting;
    private String searchLearningType;
    private String searchTarget;
    private String searchKeyword;
    private String searchResult;
    private String searchResultStatusType;
}



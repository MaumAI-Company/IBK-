package kr.co.ibk.model;

import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CardLearningDataForm extends PageForm {
    /*검색조건*/
    private String searchKeyword;
    private String searchType;
    private LocalDate stDt;
    private LocalDate edDt;

    private String pagingAt;
}



package kr.co.ibk.model;

import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TemplateForm extends PageForm {
    /*검색조건*/
    private String searchTarget;
    private String pagingAt;

    /*save*/
    private Integer id;
    private String templateName;
    private String selectCon;
    private String hdqrBobDcd;
    private String memId; // reg/mod 함께 사용

    /*delete*/
    private Integer[] idArr;
}



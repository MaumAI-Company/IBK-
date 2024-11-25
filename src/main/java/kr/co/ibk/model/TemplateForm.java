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
}



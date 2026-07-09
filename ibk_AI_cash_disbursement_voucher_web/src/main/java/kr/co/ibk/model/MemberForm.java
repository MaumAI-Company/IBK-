package kr.co.ibk.model;

import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberForm extends PageForm {
    private Integer memSeq;
    private String memSno;
}



package kr.co.ibk.model;

import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardInputForm extends PageForm {
    private String pagingAt;
    private String sorting;

    private String tstmYmd;
    private String tstmNo;
    private String brcd;
    private String loadType;
}



package kr.co.ibk.model;

import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillInputForm extends PageForm {
    private String pagingAt;
    private String sorting;

    private String txblSrn;
    private String baseYm;
    private String brcd;
    private Integer no;
    private String loadType;
    private String sortingTarget;
}
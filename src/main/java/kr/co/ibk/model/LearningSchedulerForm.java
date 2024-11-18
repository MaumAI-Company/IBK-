package kr.co.ibk.model;

import kr.co.ibk.domain.enums.TermType;
import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class LearningSchedulerForm extends PageForm {
    //todo cofls : 필요 시 검색조건 추가
    private String pagingAt;

    //insert/update
    private String schedNm;
    private String hdqrBobDcd;
    private TermType termTy;
    private LocalDate stYmd;
    private LocalTime stTime;
    private String useAt;
    private String regId;
    private String modId;
    private Integer learningModelId;

    private Integer schedId;
}



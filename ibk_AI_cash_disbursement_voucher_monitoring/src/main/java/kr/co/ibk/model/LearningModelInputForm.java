package kr.co.ibk.model;

import kr.co.ibk.domain.enums.InOutGbnType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LearningModelInputForm {
    private String colName; //컬럼명
    private Integer modelId; //모델ID
    private InOutGbnType inoutGbn;    //인/아웃 구분
}



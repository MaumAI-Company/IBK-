package kr.co.ibk.domain.web;

import kr.co.ibk.domain.enums.InOutGbnType;
import kr.co.ibk.domain.enums.InputColumnCardType;
import kr.co.ibk.domain.enums.OutputColumnCardType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LearningModelInputInfo {
    private String colName; //컬럼명
    private Integer modelId; //모델ID
    private InOutGbnType inoutGbn;    //인/아웃 구분

    private InputColumnCardType inputColumnType;
    private String inputColumnNm;
    private String inputColumnVal;
    private OutputColumnCardType outputColumnType;
    private String outputColumnNm;
}



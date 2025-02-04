package kr.co.ibk.domain.web;

import kr.co.ibk.domain.enums.*;
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

    private InputColumnCardType inputColumnCardType;
    private InputColumnBillType inputColumnBillType;
    private String inputColumnNm;
    private String inputColumnVal;
    private OutputColumnCardType outputColumnCardType;
    private OutputColumnBillType outputColumnBillType;
    private String outputColumnNm;
}



package kr.co.ibk.model;

import kr.co.ibk.domain.enums.LearningType;
import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class LearningDataForm extends PageForm {
    /*save*/
    private Integer id;
    private String dataName;  //'학습 데이터명',
    private String selectCon;
    private String hdqrBobDcd;
    private String memId;
    private String startDt;
    private String endDt;
    private LearningType learningType;
    private String templateAt;
    private Integer templateId;
    private String templateName;
    List<Map<String, Object>> inputArr;
    List<Map<String, Object>> outputArr;

    /*검색조건*/
    private String statusType;
    //private String learningType;
    private String sorting;
    private String pagingAt;

}



package kr.co.ibk.domain.web;

import kr.co.ibk.domain.enums.LearningType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateInfo {
    private String id;
    private String templateName; /*템플릿명*/
    private String selectCon; /*선택조건*/
    private String hdqrBobDcd; /*1 본부, 2 영업점*/
    private String regId; /*등록자*/
    private LocalDateTime regDt; /*등록일자*/
    private String modId; /*수정자*/
    private LocalDateTime modDt; /*수정일자*/
    private LearningType learningType;

    private String regName; /*등록자*/
    private Integer schedulerCnt;

    /*deail*/
    private List<TemplateInputInfo> inputList;
    private List<TemplateInputInfo> outputList;
}



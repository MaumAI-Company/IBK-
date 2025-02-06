package kr.co.ibk.domain.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LearningSchedulerInfo {
    /**
     * 학습 스케줄러 테이블
     */
    private String schedId; /*학습 스케줄러 ID*/
    private String schedNm; /*학습 스케줄러명*/
    private String hdqrBobDcd; /*학습 대상 : 1 본부, 2 영업점*/
    private String termTy; /*학습 주기*/
    private String stYmd; /*학습 시작 일자*/
    private String stTime; /*학습 시작 시간*/
    private String useAt; /*사용여부*/
    private String regId; /*등록자*/
    private String regDt; /*등록일시*/
    private String modId; /*수정자*/
    private String modDt; /*수정일자*/
    private String delAt; /*삭제여부*/
    private String learningModelId; /*모델관리 ID*/
    private Long templateId; /* 템플릿 ID*/

}



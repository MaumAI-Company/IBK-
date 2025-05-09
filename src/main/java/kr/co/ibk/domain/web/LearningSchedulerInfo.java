package kr.co.ibk.domain.web;

import kr.co.ibk.domain.enums.LearningType;
import kr.co.ibk.domain.enums.TermType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LearningSchedulerInfo {
    /**
     * 학습 스케줄러 테이블
     */
    private Integer schedId; /*학습 스케줄러 ID*/
    private String schedNm; /*학습 스케줄러명*/
    private String hdqrBobDcd; /*학습 대상 : 1 본부, 2 영업점*/
    private TermType termTy; /*학습 주기*/
    private String stYmd; /*학습 시작 일자*/
    private String stTime; /*학습 시작 시간*/
    private String useAt; /*사용여부*/
    private String regId; /*등록자*/
    private LocalDateTime regDt; /*등록일시*/
    private String modId; /*수정자*/
    private LocalDateTime modDt; /*수정일자*/
    private String delAt; /*삭제여부*/
    private String learningModelId; /*모델관리 ID*/
    private Integer templateId; /* 템플릿 ID*/
    private String des; /* 배치설명 */

    private String templateName; /* 템플릿 명*/
    private String regNm; /* 등록자 */
    private String epoch;
    private String learningRate;
    private String batchSize;
    // Todo : 최근 동작일시를 learning_data, learning_model의 동작 시간에 의존하지 않고 자체적으로 업데이트 할 수 있도록 컬럼 추가 필요... (배치 동작일시를 의미하기 때문)
    private LocalDateTime latestRunDt; // 최근 동작 일시
    private LearningType learningType;
    private String searchMm;
    private Integer runCnt; // 실행횟수

}



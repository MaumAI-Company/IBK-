package kr.co.ibk.domain.web;

import kr.co.ibk.domain.enums.LearningType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeployHistoryInfo {
    private Integer id;
    private Integer modelId; // 모델 ID
    private String learnNm; // 모델명
    private String execId; // 실행자 ID
    private LocalDateTime execDt; // 실행일시
    private String execName; // 실행자명
    private String result; // 배포실행결과
    private LearningType learningType; // 구분
    private String hdqrBobDcd; // 학습 대상 : 1 본부, 2 영업점

}
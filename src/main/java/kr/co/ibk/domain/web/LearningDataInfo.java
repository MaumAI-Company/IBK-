package kr.co.ibk.domain.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LearningDataInfo {
    private String id;
    private String dataName; //학습데이터명
    private String selectCon; //선택조건
    private String hdqrBobDcd; //1 본부, 2 영업점
    private String regId;
    private LocalDateTime regDt;
    private String modId;
    private LocalDateTime modDt;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private Integer templateId; //템플릿ID
}



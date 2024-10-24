package kr.co.ibk.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LearningModelForm {
    private Integer id;
    private Integer learningId;  //'학습 키',
    private String regId;  //'등록자',
    private LocalDateTime regDt;  //'등록일자',
    private String modId;  //'수정자',
    private LocalDateTime modDt;  //'수정일자',
    private String learnName;  //'학습 명',
    private String epoch;  //'학습 데이터 셋을 학습하는 회수',
    private String learningRate;  //'학습률 , 최적 값을 찾기 위한 기울기의 이동 정도',
    private String batchSize;  //'학습 데이터셋 중 , 몇 개의 데이터를 묶어서 가중치 값을 갱신할 지 지정하는 단위',
    private String learningResult;
    private String deployStatus;  //'0: 배포안됨, 1: 오류, 2: 배포완료, 3: 롤백완료, 4: 배포중',
    private LocalDateTime deployDt;  //'배포일',
    private LocalDateTime rollbackDt;   //'롤백일',
    private String resultCode;  //결과 코드',
    private String resultMsg;  //결과 메시지',
    private LocalDateTime createDtm;   //'생성시간',
    private String deleteYn;  //'N: 정상, Y: 삭제된 데이터',
    private String filePath;    //학습파일 경로
    private String fileName;    //학습파일명

    private String[] inputArr;
    private String[] outputArr;
    private Integer[] idArr;
}



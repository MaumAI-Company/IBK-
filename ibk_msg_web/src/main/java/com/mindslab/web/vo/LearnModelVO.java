package com.mindslab.web.vo;

import java.util.Date;

import com.mindslab.web.paging.PageDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LearnModelVO extends PageDTO{
	/* 추가정보  */
	private String memName;

	/* LEARN */
    private String id;// 시퀀스
    private String learningId;// 학습 키 값
	private String regId;// 등록자 ID
	private Date regDt;// 등록일자
	private String modId;// 수정자 ID
	private Date modDt;// 수정일자
	private String learnName;// 학습 명
	
	private String epoch; // 데이터셋
	private String learningRate;// 학습률
	private String batchSize;// 가중치 값	
	private String learningResult;// 학습결과
	
	private String deployStatus;// 배포상태
	private Date deployDt;// 배포일시
	private Date rollbackDt;// 롤백일시
	
	private String resultCode;// 결과 코드
	private String resultMsg;// 결콰 메시지
	private Date createDtm; // 생성시간
	private String deleteYn;// 삭제여부
	
	private String createDtmOrd;
	private String deployDtOrd;
	private String rollbackDtOrd;

    public String getCreateDtmOrd() {
        if (this.createDtmOrd == null || this.createDtmOrd.length() < 1) {
            this.createDtmOrd = "desc";
        }
        return this.createDtmOrd;
    }
    public String getDeployDtOrd() {
        if (this.deployDtOrd == null || this.deployDtOrd.length() < 1) {
            this.deployDtOrd = "desc";
        }
        return this.deployDtOrd;
    }
    public String getRollbackDtOrd() {
		if (this.rollbackDtOrd == null || this.rollbackDtOrd.length() < 1) {
			this.rollbackDtOrd = "desc";
		}
		return this.rollbackDtOrd;
	}
	
}

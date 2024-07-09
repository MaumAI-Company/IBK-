package com.mindslab.web.vo;

import java.util.Date;

import com.mindslab.web.paging.PageDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LearnHistoryVO extends PageDTO{
	/* 추가정보  */
	private String memName;

	/* LEARN */
	private String id;// 키 값
    private String learningId;// 학습 키 값
    private String learningModelId;// 학습모델 키 값
	private String learnName;// 학습 명
	private String threshold;// 배포시 스레스 홀드 값
	private String regId;// 등록자
	private Date regDt;// 등록일자
	private String modId;// 수정자
	private Date modDt;// 수정일자
	private String useStatus;// 사용여부 (0:중지, 1: 사용 ) 
	private String deployStatus;//배포상태 (0:실패, 1: 성공 )
    private Date deployDt;// 배포일
    private Date rollbackDt;// 롤백일
    private String resultCode;// 결과 코드
    private String resultMsg;// 결과 메시지

	/* 검색 조건  */
	private String searchUse; // 사용 상태
	private String searchDeploy; // 배포여부 
	private String deployDtOrd;// 배포일시 기준 정렬순서
	private String rollbackDtOrd;// 롤백일시 기준 정렬순서

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

package com.mindslab.web.vo;

import java.util.Date;

import com.mindslab.web.paging.PageDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LearnVO extends PageDTO{
	/* 추가정보  */
	private String memName;

	/* LEARN */
	private String id;// 시퀀스
	private String regId;// 등록자 ID
	private Date regDt;// 등록일자
	private String modId;// 수정자 ID
	private Date modDt;// 수정일자
	private String learnName;// 학습 명
	
	private String learnFileName;// 원본 파일명 
	private String learnFileSaveName;// 원본 파일 저장명
	private String learnFileUrl;// 원본 파일 저장경로
	private long learnFileSize;// 원본 사이즈
	private Date learnFileUploadDt;// 원본 등록일
	private String learnStatus;// 학습상태 : 0: 등록안됨, 1: 오류, 2: 등록완료, 3: 학습중, 4: 학습완료
	
	private String epoch; // 데이터셋
	private String learningRate;// 학습률
	private String batchSize;// 가중치 값
	
	private String answerFileName;// 정답셋 파일명
	private String answerFileSaveName;// 정답셋 파일 저장명
	private String answerFileUrl;// 정답셋 저장경로
	private long answerFileSize;// 정답셋 사이즈
	private Date answerFileUploadDt;// 정답셋 등록일
	private String answerStatus;// 정답셋 상태
	
	private String learningResult;// 학습결과
	
	private String deployStatus;// 배포상태
	private Date deployDt;// 배포일시
	private Date rollbackDt;// 롤백일시
	
	private String deleteYn;// 삭제여부
	
	private String learnFileUploadDtOrd;
	private String answerFileUploadDtOrd;
	private String deployDtOrd;
	private String rollbackDtOrd;

    public String getLearnFileUploadDtOrd() {
		if (this.learnFileUploadDtOrd == null || this.learnFileUploadDtOrd.length() < 1) {
			this.learnFileUploadDtOrd = "desc";
		}
		return this.learnFileUploadDtOrd;
	}
    public String getAnswerFileUploadDtOrd() {
		if (this.answerFileUploadDtOrd == null || this.answerFileUploadDtOrd.length() < 1) {
			this.answerFileUploadDtOrd = "desc";
		}
		return this.answerFileUploadDtOrd;
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

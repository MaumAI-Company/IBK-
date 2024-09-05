package com.mindslab.web.vo;

import java.util.Date;

import com.mindslab.web.paging.PageDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MsgVO extends PageDTO{
	
	/* MSG_RESULT */
	private String id;// 아이디
	private String channel;// 채널
	private String brCode;// 부점코드
	private String emCode;// 직원번호
	private String msgTitle;// 문자제목
	private String msgContext;// 문자내용
	private String keyText;// 검증문구
	private String keyStartIndex;// 검증문구 시작점
	private String keyEndIndex;// 검증문구 종료지점
	private String selectLabel;// 선택목적 > 선택조건
	private String resultLabel;// 검증결과 > 검증목적
	private String isMatch;// 일치여부
	private String percentage;// 확률
	private String url;// URL >> 추후 화면제공시 필요한 URL 패턴 규칙 필요 
	private Date createDtm;// 검증 일시

	
	/* 검색 조건  */
	private String searchChannel; // 채널
	private String searchSelect; // 발송목적
    private String searchResult; // 검증결과
    private String searchFit; // 일치여부
	private String createDtmOrd; // 검증날짜 기준 정렬조건 ORDER BY : asc / desc
	
	
	public String getCreateDtmOrd() {
		if (this.createDtmOrd == null || this.createDtmOrd.length() < 1) {
			this.createDtmOrd = "desc";
		}
		return this.createDtmOrd;
	}
	
	
}

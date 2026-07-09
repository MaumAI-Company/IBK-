package com.mindslab.web.vo;

import java.util.Date;

import com.mindslab.web.paging.PageDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScanVO extends PageDTO{

	/* SCAN_RESULT */
	private String id; //  '아이디'
	private String fileName; //  '파일이름'
	private String level; //  '탐지 레벨(1:low, 2:middel, 3:high)'
	private String detectResult; //  '탐지 결과'
	private String detectLog; //  '탐지 결과 내용'
	private Date createDtm; //  '생성날짜'
	private String edpsCsn; //  '전산고객번호'
	private String docId; //  'BPR이미지ID'
	private String isBatch; //  '배치여부'
	private String realCell; //  '진짜'
	private String monitorCell; //  '모니터'
	private String paperCell; //  '종이'
	
	private String createDtmOrd; // 생성날짜 기준 ORDER BY : asc / desc
	private String searchEdpsCsn; // 생성날짜 기준 ORDER BY : asc / desc
	
	public String getCreateDtmOrd() {
		if (this.createDtmOrd == null || this.createDtmOrd.length() < 1) {
			this.createDtmOrd = "asc";
		}
		return this.createDtmOrd;
	}
	
}

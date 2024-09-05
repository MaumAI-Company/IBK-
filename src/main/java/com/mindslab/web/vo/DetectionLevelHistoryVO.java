package com.mindslab.web.vo;

import java.util.Date;

import com.mindslab.web.paging.PageDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DetectionLevelHistoryVO extends PageDTO{

	/* HC_DETECTION_LEVEL_HISTORY */
	private String seq; // '탐지레벨 시퀀스'
	private String level; // '탐지레벨'
	private String prevLevel; // '이전탐지레벨'
	private String levelStrng; // '탐지레벨 강도'
	private String prevLevelStrng; // '탐지레벨 강도'
	private String levelDescription; // '레벨 설명'
	private String prevLevelDescription; // '이전 레벨 설명'
	private String updClientIp; // '클러이언트 IP'
	private String lastModMemId; // '변경 유저 ID'
	private String lastModDate; // '변경일자'
}

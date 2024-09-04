package kr.co.ibk.domain.web;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetectionLevel {
	/* HC_DETECTION_LEVEL */
	private String seq; // '탐지레벨 시퀀스'
	private String level; // '탐지레벨'
	private String levelStrng; // '탐지레벨 강도'
	private String levelDescription; // '레벨 설명'
	private String useStatus; // '사용여부(0: 사용, 1:미사용)'
	private String regMemId; // '생성 유저ID'
	private Date regDate; // '생성일자'
	private String updMemId; // '수정 유저 ID'
	private Date updDate; // '수정일자'
	private String rm; // '비고'
}

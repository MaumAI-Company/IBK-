package com.mindslab.web.vo;

import lombok.ToString;

import java.util.Date;

import lombok.Data;

@Data
@ToString
public class MenuVO{
	
	/* HC_MENU */
	private String menuSeq; // '메뉴일련번호'
	private String roleId; // '메뉴사용가능한역할'
	private String parId; // '상위메뉴아이디'
	private String menuId; // '메뉴아이디'
	private String menuCode; // '메뉴코드'
	private String menuName; // '메뉴명'
	private String menuEngName; // '메뉴명(영문)'
	private String menuDepth; // '노드 깊이'
	private String menuOrder; // '노드 순서'
	private String menuType; // 'STOP, PAGE, LINK, EMBEDED 등 연결 구분'
	private String menuStat; // 'NORMAL, REST, STOP, DELETE 등 상태'
	private String menuRegId; // '등록자'
	private Date menuRegDt; // '등록일시'
	private String menuModId; // '변경자'
	private Date menuModDt; // '변경일시'
	private String menuColumn1; // '컬럼1'
	private String menuColumn2; // '컬럼2'
	private String menuColumn3; // '컬럼3'
	private String menuColumn4; // '컬럼4'
	private String menuColumn5; // '컬럼5'	
}

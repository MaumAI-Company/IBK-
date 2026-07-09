package kr.co.ibk.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageNavigationForm implements Serializable{

	private static final long serialVersionUID = 12L;

	/* 총갯수 */
	private int totalCount;

	/* 기본페이지 */
	private int firstPage;

	/* 페이지당 행수 */
	private int rowPageCount;

	/* 현재페이시 번호 */
	private int indexPage;

	/* 네이게이션에 보일 숫자수 */
	private int blockCount;

	/* 마지막 페이지 */
	private int lastPage;

	/* 현재 데이터 No */
	private int dataNo;
}
package kr.co.ibk.model;

import java.util.Date;

import kr.co.ibk.model.paging.PageForm;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeptForm extends PageForm{
	/* HC_DEPT */
	private String deptSeq; //  '부서일련번호'
	private String parId; //  '상위부서아이디'
	private String deptId; //  '부서아이디'
	private String deptCode; //  '부서코드'
	private String deptName; //  '부서명'
	private String deptEngName; //  '부서명(영문)'
	private String deptDepth; //  '노드 깊이'
	private String deptOrder; //  '노드 순서'
	private String deptStat; //  'NORMAL, REST, STOP, DELETE 등 상태'
	private String deptRegId; //  '등록자'
	private Date deptRegDt; //  '등록일시'
	private String deptModId; //  '변경자'
	private Date deptModDt; //  '변경일시'
	private String deptColumn1; //  '컬럼1'
	private String deptColumn2; //  '컬럼2'
	private String deptColumn3; //  '컬럼3'
	private String deptColumn4; //  '컬럼4'
	private String deptColumn5; //  '컬럼5'

	private String deptPath; // 부서 경로
}

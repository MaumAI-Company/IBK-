package kr.co.ibk.model.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PageForm extends Criteria {
	/** 페이징 정보 */
	private PaginationInfo paginationInfo;
}

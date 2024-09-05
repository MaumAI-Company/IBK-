package com.mindslab.web.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDTO extends Criteria {
	
	/** 페이징 정보 */
	private PaginationInfo paginationInfo;
}

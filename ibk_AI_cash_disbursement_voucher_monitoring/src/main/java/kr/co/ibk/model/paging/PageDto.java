package kr.co.ibk.model.paging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageDto {
	private int currentPage;
	private int totalPageCount;
	private boolean hasPreviousPage;
	private boolean hasNextPage;
	private int firstPage;
	private int lastPage;
    private int totalRecordCount;
    private int recordsPerPage;

	public PageDto(PaginationInfo paginationInfo, int recordsPerPage) {
		this.currentPage = paginationInfo.getCriteria().getCurrentPageNo();
		this.totalPageCount = paginationInfo.getTotalPageCount();
		this.hasPreviousPage = paginationInfo.isHasPreviousPage();
		this.hasNextPage = paginationInfo.isHasNextPage();
		this.firstPage = paginationInfo.getFirstPage();
		this.lastPage = paginationInfo.getLastPage();
		this.totalRecordCount = paginationInfo.getTotalRecordCount();
		this.recordsPerPage = recordsPerPage;
	}
}

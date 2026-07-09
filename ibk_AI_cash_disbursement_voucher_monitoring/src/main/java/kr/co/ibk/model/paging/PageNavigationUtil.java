package kr.co.ibk.model.paging;

import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.model.PageNavigationForm;

public class PageNavigationUtil {
	public PageNavigationUtil(){
		// Auto-generated constructor stub
	}

	public static CustomMap createNavigationInfo(CustomMap dataMap){

		PageNavigationForm pnv = new PageNavigationForm();

		// 전체 건수
		pnv.setTotalCount(dataMap.getInt("totalCount"));

		// 한 페이지에 보여 줄 row 수  
		pnv.setRowPageCount(
				Integer.parseInt(dataMap.getString("rowPageCount", "10")));

		// 페이지네이션에 사용 할 indexPage
		pnv.setIndexPage(Integer
				.parseInt(dataMap.getString(ConstantUtil.pageIndex, "1")));

		// 페이지 네비게이션으로 사용할 block 수 
		pnv.setBlockCount(
				Integer.parseInt(dataMap.getString("blockCount", "5")));

		// 마지막페이지
		int lastPage = pnv.getTotalCount() / pnv.getRowPageCount();

		//나머지가 존재할경우 1페이지 추가를 위한 변수
		int dummyPage = 0;
		if (pnv.getTotalCount() % pnv.getRowPageCount() > 0){ //나머지가 존재할경우 1페이지 추가		
			dummyPage = 1;
		}

		//마지막페이지
		pnv.setLastPage(lastPage + dummyPage);

		// 다음페이지가 있는지 
		int plusPage = pnv.getIndexPage() % pnv.getBlockCount() == 0
				? -1 * pnv.getBlockCount() + 1
				: 1;

		// 첫번째 페이지
		pnv.setFirstPage(
				pnv.getIndexPage() / pnv.getBlockCount() * pnv.getBlockCount()
						+ plusPage);

		// 첫번째 목록 번호 
		pnv.setDataNo(pnv.getTotalCount()
				- ((pnv.getIndexPage() - 1) * pnv.getRowPageCount()));

		//map.put("pageNavigationVo", pnv);

		dataMap.put("limitStart",
				(pnv.getIndexPage() - 1) * pnv.getRowPageCount());
		dataMap.put("limitCount", pnv.getRowPageCount());

		dataMap.put("limitEnd", pnv.getRowPageCount());

		return dataMap;
	}
}

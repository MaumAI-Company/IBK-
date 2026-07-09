package com.mindslab.web.common.support.utils.excel;

import java.util.HashMap;

public class MsgResultStatisticExcelDown extends ExcelHandler {
	public MsgResultStatisticExcelDown(String[] header, String fileName, String sheetName, int size) {
		super.ExcelHandler(header, fileName, sheetName, size);
	}

	@Override
	public void createExcelBody(HashMap<String, Object> map) {

      	int cellNum = 0;
      	
  		//1. row생성
   	    this.objRow = objSheet.createRow(this.rowNum++);
    	
   	    //2-1. cell생성
   	    //2-2. 값매핑
   	    // ......(각자 요구사항에 맞게 데이터 매핑)
   	    //setExcelCell(cellNum++, this.dataIdx++);

		//String[] header = {"부점코드","전체","기존계약유지","고객관리","심의필 마케팅", "미심의 광고문자"};
   	    Long select1 = (Long) map.get("SELECT1");
   	    Long select2 = (Long) map.get("SELECT2");
   	    Long select3 = (Long) map.get("SELECT3");
   	    Long select4 = (Long) map.get("SELECT4");
   	    Long all = select1 + select2 + select3 + select4;
   	    setExcelCell(cellNum++, nvl(map.get("DATE")));
	   	setExcelCell(cellNum++, nvl(all));
	   	setExcelCell(cellNum++, nvl(select1));
	   	setExcelCell(cellNum++, nvl(select2));
	   	setExcelCell(cellNum++, nvl(select3));
	   	setExcelCell(cellNum++, nvl(select4));

	}
	
	public String nvl(Object obj) {
		if(obj == null) {
			return "";
		} else if (obj instanceof Integer) {
			return Integer.toString((Integer)obj);
		} else if (obj instanceof Long) {
			return Long.toString((Long)obj);
		} else {
			return (String) obj;
		}
	}
}

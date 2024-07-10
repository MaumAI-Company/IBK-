package com.mindslab.web.common.support.utils.excel;

import java.util.HashMap;

public class StatisticExcelDown extends ExcelHandler {
	public StatisticExcelDown(String[] header, String fileName, String sheetName, int size) {
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
   	    setExcelCell(cellNum++, nvl(map.get("DATE")));
	   	setExcelCell(cellNum++, nvl(map.get("REAL")));
	   	setExcelCell(cellNum++, nvl(map.get("PAPER")));
	   	setExcelCell(cellNum++, nvl(map.get("MONITOR")));

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

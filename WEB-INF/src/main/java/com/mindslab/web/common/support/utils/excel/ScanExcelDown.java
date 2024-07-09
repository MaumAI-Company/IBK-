package com.mindslab.web.common.support.utils.excel;

import java.util.HashMap;

public class ScanExcelDown extends ExcelHandler {
	public ScanExcelDown(String[] header, String fileName, String sheetName, int size) {
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
   	    

   	    setExcelCell(cellNum++, nvl(map.get("FILE_NAME")));
   	    setExcelCell(cellNum++, map.get("CREATE_DTM"));
	   	setExcelCell(cellNum++, nvl(map.get("EDPS_CSN_STR")));
	   	setExcelCell(cellNum++, nvl(map.get("LEVEL_STR")));
	   	setExcelCell(cellNum++, nvl(map.get("DETECT_RESULT")));
	   	setExcelCell(cellNum++, nvl(map.get("REAL_CELL_STR")));
	   	setExcelCell(cellNum++, nvl(map.get("MONITOR_CELL_STR")));
	   	setExcelCell(cellNum++, nvl(map.get("PAPER_CELL_STR")));
	   	setExcelCell(cellNum++, nvl(map.get("DETECT_LOG")));
	   	setExcelCell(cellNum++, nvl(map.get("DOC_ID")));

	}
	
	public String nvl(Object obj) {
		if(obj == null) {
			return "";
		} else {
			return (String) obj;
		}
	}
}

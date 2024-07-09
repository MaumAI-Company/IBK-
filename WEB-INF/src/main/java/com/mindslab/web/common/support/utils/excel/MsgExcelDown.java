package com.mindslab.web.common.support.utils.excel;

import java.util.HashMap;

public class MsgExcelDown extends ExcelHandler {
	public MsgExcelDown(String[] header, String fileName, String sheetName, int size) {
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
    	//String[] header = {"CHANNEL", "CREATE_DTM", "BR_CODE", "EM_CODE", "MSG_TITLE", "SELECT_LABEL", "RESULT_LABEL", "IS_MATCH"};
   	    
   	    String match_str = (String) map.get("IS_MATCH_STR");
   	    String str = "";
   	    if ("1".equals(match_str)) {
   	    	str = "일치"; //일치 -> 통과 로 변경 요망 (∵2023-01-26 고객 요구사항으로 인해)
   	    }else if ("0".equals(match_str)) {
   	    	str = "불일치"; //불일치 -> 불통과 로 변경 요망 (∵2023-01-26 고객 요구사항으로 인해)
   	    }
    	
   	    setExcelCell(cellNum++, nvl(map.get("CHANNEL")));
   	    setExcelCell(cellNum++, map.get("CREATE_DTM"));
	   	setExcelCell(cellNum++, nvl(map.get("BR_CODE")));
	   	setExcelCell(cellNum++, nvl(map.get("EM_CODE")));
	   	setExcelCell(cellNum++, nvl(map.get("MSG_TITLE")));
	   	setExcelCell(cellNum++, nvl(map.get("SELECT_LABEL")));
	   	setExcelCell(cellNum++, nvl(map.get("RESULT_LABEL")));
        setExcelCell(cellNum++, nvl(str));
        setExcelCell(cellNum++, nvl(map.get("PERCENTAGE")));
		/*2023-01-27 add start */
		setExcelCell(cellNum++, nvl(map.get("MSG_CONTEXT")));
		/* 2023-01-27 add end */

	}
	
	public String nvl(Object obj) {
		if(obj == null) {
			return "";
		} else {
			return (String) obj;
		}
	}
}

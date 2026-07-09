
package com.mindslab.web.common.vo;

import java.util.List;
import java.util.Map;

public class ErrorRestMessageVO{
	private String errCode;

	private String errMessage;

	private List<Map<String, String>> errDataList;

	public String getErrCode(){
		return errCode;
	}

	public void setErrCode(String errCode){
		this.errCode = errCode;
	}

	public String getErrMessage(){
		return errMessage;
	}

	public void setErrMessage(String errMessage){
		this.errMessage = errMessage;
	}

	public List<Map<String, String>> getErrDataList(){
		return errDataList;
	}

	public void setErrDataList(List<Map<String, String>> errDataList){
		this.errDataList = errDataList;
	}
}

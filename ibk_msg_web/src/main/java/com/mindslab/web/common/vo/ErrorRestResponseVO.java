
package com.mindslab.web.common.vo;

public class ErrorRestResponseVO{

	private ResponseHeaderVO header = new ResponseHeaderVO();
	private ErrorBodyVO body = new ErrorBodyVO();

	public ResponseHeaderVO getHeader(){
		return header;
	}

	public void setHeader(ResponseHeaderVO header){
		this.header = header;
	}

	public ErrorBodyVO getBody(){
		return body;
	}

	public void setBody(ErrorBodyVO body){
		this.body = body;
	}
}

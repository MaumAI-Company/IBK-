
package com.mindslab.web.common.vo;

public class ResponseVO<T>{
	private ResponseHeaderVO header = new ResponseHeaderVO();
	private ResponseBodyVO<T> body = new ResponseBodyVO<T>();

	public ResponseHeaderVO getHeader(){
		return header;
	}

	public void setHeader(ResponseHeaderVO header){
		this.header = header;
	}

	public ResponseBodyVO<T> getBody(){
		return body;
	}

	public void setBody(ResponseBodyVO<T> body){
		this.body = body;
	}

}

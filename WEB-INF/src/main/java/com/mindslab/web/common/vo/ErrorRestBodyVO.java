
package com.mindslab.web.common.vo;

public class ErrorRestBodyVO{
	private ErrorMessageVO docs = new ErrorMessageVO();

	public ErrorMessageVO getDocs(){
		return docs;
	}

	public void setDocs(ErrorMessageVO docs){
		this.docs = docs;
	}
}

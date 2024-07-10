
package com.mindslab.web.common.vo;

import java.util.ArrayList;
import java.util.List;

public class ResponseBodyVO<T>{
	private int docCnt = 0;

	private List<T> docs = new ArrayList<T>();

	public int getDocCnt(){
		return docCnt;
	}

	public void setDocCnt(int docCnt){
		this.docCnt = docCnt;
	}

	public List<T> getDocs(){
		return docs;
	}

	public void setDocs(List<T> docs){
		this.docs = docs;
	}

	public void setDoc(T doc){
		this.docs.add(doc);
	}
}

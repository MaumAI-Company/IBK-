package com.mindslab.web.common.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageVO {
    private String errCode;
	private String errMessage;
    private List<Map<String, String>> errDataList = new ArrayList<>();

    public void addReturnMsg(Map<String, String> map) {
		errDataList.add(map);
	}

	public void addReturnMsg(String field, String message) {
		Map<String, String> map = new HashMap<>();
		map.put(field, message);
		errDataList.add(map);
	}
}

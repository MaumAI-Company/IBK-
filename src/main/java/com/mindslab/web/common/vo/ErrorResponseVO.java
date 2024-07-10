package com.mindslab.web.common.vo;

import lombok.Data;

@Data
public class ErrorResponseVO {
    private ErrorHeaderVO header = new ErrorHeaderVO();
	private ErrorBodyVO body = new ErrorBodyVO();
}

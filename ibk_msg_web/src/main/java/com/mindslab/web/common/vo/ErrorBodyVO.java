package com.mindslab.web.common.vo;

import lombok.Data;

@Data
public class ErrorBodyVO {
    private ErrorMessageVO docs = new ErrorMessageVO();
}

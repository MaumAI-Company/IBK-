package com.mindslab.web.auth;

import com.mindslab.web.common.error.ErrorCode;

public enum AuthErrorCode implements ErrorCode{
	ACCESS_DENIED("AUT0N00-403"),
	INVALID_PASSWORD("AUT0N01-401"),
	NOTFOUND_USERID("AUT0N02-404"),
	INVALID_STATUS("AUT0N03-401"),
	PASSWORD_FAILCOUNT("AUT0N04-401"),
	IP_ACCESS_DENIED("AUT0N05-403");

	private String errorCode;

	AuthErrorCode(String errorCode){
		this.errorCode = errorCode;
	}

	@Override
	public String getErrorCode(){
		return errorCode;
	}
}

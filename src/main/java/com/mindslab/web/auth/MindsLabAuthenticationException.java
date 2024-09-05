package com.mindslab.web.auth;

import com.mindslab.web.common.error.ErrorCode;
import org.springframework.security.core.AuthenticationException;

//security 권한 인증 custom Exception 처리
public class MindsLabAuthenticationException extends AuthenticationException{
	private ErrorCode errorCode;

	public MindsLabAuthenticationException(String msg){
		super(msg);
	}

	public MindsLabAuthenticationException(String msg, Throwable t){
		super(msg, t);
	}

	public MindsLabAuthenticationException(String msg, ErrorCode errorCode){
		super(msg);

		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode(){
		return errorCode;
	}
}

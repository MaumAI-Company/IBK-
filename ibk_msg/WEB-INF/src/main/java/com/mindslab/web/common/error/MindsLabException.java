package com.mindslab.web.common.error;

import org.springframework.validation.FieldError;

public class MindsLabException extends RuntimeException {


	private ErrorCode errorCode;
	private String message;
	Object extraInformation;

	public MindsLabException(Throwable e){
		super(e);

		if (e instanceof MindsLabException){
			MindsLabException exception = (MindsLabException) e;

			this.errorCode = exception.getErrorCode();
			this.message = exception.getMessage();
		}
	}

	public MindsLabException(FieldError fieldError){
		super();

		this.errorCode = CommonErrorCode.BAD_REQUEST;
		this.message = fieldError.getField() + " - "
				+ fieldError.getDefaultMessage();
	}

	public MindsLabException(ErrorCode errorCode){
		super();
		this.errorCode = errorCode;
	}

	public MindsLabException(ErrorCode errorCode, String message){
		super(message);
		this.errorCode = errorCode == null ? getDefaultCode() : errorCode;
		this.message = message;

	}

	public MindsLabException(ErrorCode errorCode, String message,
			Throwable cause){
		super(message, cause);
		this.errorCode = errorCode == null ? getDefaultCode() : errorCode;
		this.message = message;
	}

	public MindsLabException(ErrorCode errorCode, Throwable cause){
		this.errorCode = getDefaultCode();
	}

	public MindsLabException(ErrorCode errorCode, String message,
			Object extraInformation){
		super();
		this.errorCode = errorCode;
		this.message = message;
		this.extraInformation = extraInformation;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public ErrorCode getErrorCode(){
		return this.errorCode;
	}

	public String getMessage(){
		return this.message;
	}

	protected ErrorCode getDefaultCode(){
		return CommonErrorCode.INTERNAL_SERVER_ERROR;
	}

	public Object getExtraInformation(){
		return extraInformation;
	}

	public void setExtraInformation(Object extraInformation){
		this.extraInformation = extraInformation;
	}
}

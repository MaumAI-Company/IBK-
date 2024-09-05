package com.mindslab.web.common.error;

import org.springframework.validation.FieldError;

public class MindsLabRestException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5477099153945135309L;

	private ErrorCode errorCode;
	private String message;
	Object extraInformation;

	public MindsLabRestException(Throwable e){
		super(e);

		if (e instanceof MindsLabRestException){
			MindsLabRestException exception = (MindsLabRestException) e;

			this.errorCode = exception.getErrorCode();
			this.message = exception.getMessage();
		}
	}

	public MindsLabRestException(FieldError fieldError){
		super();

		this.errorCode = CommonErrorCode.BAD_REQUEST;
		this.message = fieldError.getField() + " - "
				+ fieldError.getDefaultMessage();
	}

	public MindsLabRestException(ErrorCode errorCode){
		super();

		this.errorCode = errorCode; 
	}

	public MindsLabRestException(ErrorCode errorCode, String message){
		super(message);
		this.errorCode = errorCode == null ? getDefaultCode() : errorCode;
		this.message = message;

	}

	public MindsLabRestException(ErrorCode errorCode, String message,
			Throwable cause){
		super(message, cause);
		this.errorCode = errorCode == null ? getDefaultCode() : errorCode;
		this.message = message;
	}

	public MindsLabRestException(ErrorCode errorCode, Throwable cause){
		this.errorCode = getDefaultCode();
	}

	public MindsLabRestException(ErrorCode errorCode, String message,
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

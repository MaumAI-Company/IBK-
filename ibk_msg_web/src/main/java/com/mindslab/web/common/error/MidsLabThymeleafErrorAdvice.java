package com.mindslab.web.common.error;

import com.mindslab.web.common.controller.CommonThymeleafController;
import com.mindslab.web.common.vo.ErrorResponseVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice(assignableTypes = { CommonThymeleafController.class })
public class MidsLabThymeleafErrorAdvice {

	@Autowired
	@Qualifier("messageSource")
	MessageSource messageSource;


    @ExceptionHandler(Exception.class)
	public String handleException(HttpServletRequest request,
			HttpServletResponse response, final Exception ex){
		ErrorResponseVO result = new ErrorResponseVO();
		HttpStatus status = null;
		String errorCode = null;

		if (ex instanceof HttpRequestMethodNotSupportedException){
			status = HttpStatus.METHOD_NOT_ALLOWED;
			errorCode = CommonErrorCode.METHOD_NOT_ALLOWED.getErrorCode();
		} else if (ex instanceof HttpMediaTypeNotSupportedException){
			status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
			errorCode = CommonErrorCode.UNSUPPORTED_MEDIA_TYPE.getErrorCode();
		} else if (ex instanceof HttpMediaTypeNotAcceptableException){
			status = HttpStatus.NOT_ACCEPTABLE;
			errorCode = CommonErrorCode.NOT_ACCEPTABLE.getErrorCode();
		} else if (ex instanceof MissingPathVariableException){
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode();
		} else if (ex instanceof MissingServletRequestParameterException){
			status = HttpStatus.BAD_REQUEST;
			errorCode = CommonErrorCode.BAD_REQUEST.getErrorCode();
		} else if (ex instanceof ServletRequestBindingException){
			status = HttpStatus.BAD_REQUEST;
			errorCode = CommonErrorCode.BAD_REQUEST.getErrorCode();
		} else if (ex instanceof ConversionNotSupportedException){
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode();
		} else if (ex instanceof TypeMismatchException){
			status = HttpStatus.BAD_REQUEST;
			errorCode = CommonErrorCode.BAD_REQUEST.getErrorCode();
		} else if (ex instanceof HttpMessageNotReadableException){
			status = HttpStatus.BAD_REQUEST;
			errorCode = CommonErrorCode.BAD_REQUEST.getErrorCode();
		} else if (ex instanceof HttpMessageNotWritableException){
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode();
		} else if (ex instanceof MethodArgumentNotValidException){
			status = HttpStatus.BAD_REQUEST;
			errorCode = CommonErrorCode.BAD_REQUEST.getErrorCode();
		} else if (ex instanceof MissingServletRequestPartException){
			status = HttpStatus.BAD_REQUEST;
			errorCode = CommonErrorCode.BAD_REQUEST.getErrorCode();
		} else if (ex instanceof BindException){
			status = HttpStatus.BAD_REQUEST;
			errorCode = CommonErrorCode.BAD_REQUEST.getErrorCode();
		} else if (ex instanceof NoHandlerFoundException){
			status = HttpStatus.NOT_FOUND;
			errorCode = CommonErrorCode.NOTFOUND.getErrorCode();
		} else if (ex instanceof AsyncRequestTimeoutException){
			status = HttpStatus.SERVICE_UNAVAILABLE;
			errorCode = CommonErrorCode.REQUEST_TIMEOUT.getErrorCode();
		} else{
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode();
		}

		log.error(" handleException exception = " + ex + ", status = " + status + "ErrorCode = " + errorCode);

		response.setStatus(status.value());
		response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

		result.getHeader().setStatus(status.value());
		result.getBody().getDocs().setErrCode(errorCode);
		result.getBody().getDocs().setErrMessage(messageSource
                .getMessage(errorCode, null, LocaleContextHolder.getLocale()));
		request.setAttribute("error", result);

        return "thymeleaf/errors/error";

	}
}


package com.mindslab.web.common.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mindslab.web.apis.controller.RestBprImageController;
import com.mindslab.web.common.vo.ErrorResponseVO;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ErrorRestControllerAdvice{

	@Autowired
	@Qualifier("messageSource")
	MessageSource messageSource;

	@ExceptionHandler(MindsLabRestException.class)
	public ErrorResponseVO handlePpsException(HttpServletRequest request,
			HttpServletResponse response, final MindsLabRestException exception){
		ErrorResponseVO result = new ErrorResponseVO();

		ErrorCode errorCode = exception.getErrorCode();

		if (errorCode == null){
			errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		}

		log.error(" handleRestException: errorCode = " + errorCode
				+ ", locale = " + LocaleContextHolder.getLocale());

		if (exception.getMessage() == null){
			exception.setMessage(
					messageSource.getMessage(errorCode.getErrorCode(), null,
							LocaleContextHolder.getLocale()));
		}

		response.setStatus(errorCode.getResponseCode());

		result.getHeader().setStatus(response.getStatus());
		result.getBody().getDocs().setErrCode(errorCode.getErrorCode());
		result.getBody().getDocs().setErrMessage(exception.getMessage());

		return result;
	}
}

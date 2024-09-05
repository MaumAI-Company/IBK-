package com.mindslab.web.common.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mindslab.web.auth.AuthErrorCode;
import com.mindslab.web.common.controller.CommonController;
import com.mindslab.web.common.vo.ErrorResponseVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MindsLabErrorController extends CommonController implements ErrorController{

	@Autowired
	@Qualifier("messageSource")
    MessageSource messageSource;

    private String JSP_VIEW_PATH = "errors/";
	private String THYMELEAF_VIEW_PATH = "/thymeleaf/errors/";

	@RequestMapping(value = "/errors/access-denied")
	public void accessDeniedExcepton(){
		throw new MindsLabException(AuthErrorCode.ACCESS_DENIED);
	}

	@RequestMapping(value = "/errors/entrypoint")
	public void entryPointException(){
		throw new MindsLabException(AuthErrorCode.ACCESS_DENIED);
	}

	@RequestMapping(value = "/errors/auth-service-error/{errorCode}")
	public ModelAndView authServiceException(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String errorCode) {
		ErrorResponseVO result = new ErrorResponseVO();
		ModelAndView mav = new ModelAndView();
		log.error(" authServiceException: errorCode = " + errorCode + ", locale = " + LocaleContextHolder.getLocale());

		response.setStatus(getErrorCode(errorCode));

		result.getHeader().setStatus(response.getStatus());
		result.getBody().getDocs().setErrCode(errorCode);
		result.getBody().getDocs()
				.setErrMessage(messageSource.getMessage(errorCode, null, LocaleContextHolder.getLocale()));
		mav.addObject("error", result);
		mav.setViewName(JSP_VIEW_PATH + "error");

		return mav;
	}

    @RequestMapping("/errors/{errorCode}")
    public ModelAndView handleError(HttpServletRequest request, HttpServletResponse response, @PathVariable String errorCode) {
        ErrorResponseVO result = new ErrorResponseVO();
        ModelAndView mav = new ModelAndView();
		log.error(" authServiceException: errorCode = " + errorCode + ", locale = " + LocaleContextHolder.getLocale());

		response.setStatus(getErrorCode(errorCode));

		result.getHeader().setStatus(response.getStatus());
		result.getBody().getDocs().setErrCode(errorCode);
		result.getBody().getDocs().setErrMessage(messageSource.getMessage(errorCode, null, LocaleContextHolder.getLocale()));
        mav.addObject("error", result);
        mav.setViewName(JSP_VIEW_PATH + "error");

		return mav;
    }

    @RequestMapping("/thymeleaf/errors")
    public String thymeleafHandleError(HttpServletRequest request, HttpServletResponse response) {
        log.info("response.getStatus = {}" + response.getStatus());
        return THYMELEAF_VIEW_PATH + "error";
    }

    private int getErrorCode(String errorCode) {
        return Integer.parseInt(errorCode.split("-")[1]);
    }

    @RequestMapping("errors")
    public ModelAndView handleDefualtError(HttpServletRequest request,
			HttpServletResponse response, final Exception ex) {
		ErrorResponseVO result = new ErrorResponseVO();
		HttpStatus status = null;
		String errorCode = null;
		ModelAndView mav = new ModelAndView();

		if (response.getStatus() == HttpStatus.METHOD_NOT_ALLOWED.value()) {
			status = HttpStatus.METHOD_NOT_ALLOWED;
			errorCode = CommonErrorCode.METHOD_NOT_ALLOWED.getErrorCode();
		} else if (response.getStatus() == HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()) {
			status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
			errorCode = CommonErrorCode.UNSUPPORTED_MEDIA_TYPE.getErrorCode();
		} else if (response.getStatus() == HttpStatus.NOT_ACCEPTABLE.value()) {
			status = HttpStatus.NOT_ACCEPTABLE;
			errorCode = CommonErrorCode.NOT_ACCEPTABLE.getErrorCode();
		} else if (response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode();
		} else if (response.getStatus() == HttpStatus.BAD_REQUEST.value()) {
			status = HttpStatus.BAD_REQUEST;
			errorCode = CommonErrorCode.BAD_REQUEST.getErrorCode();
		} else if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
			status = HttpStatus.NOT_FOUND;
			errorCode = CommonErrorCode.NOTFOUND.getErrorCode();
		} else if (ex instanceof AsyncRequestTimeoutException) {
			status = HttpStatus.SERVICE_UNAVAILABLE;
			errorCode = CommonErrorCode.REQUEST_TIMEOUT.getErrorCode();
		} else {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode();
		}

		log.error(" handleException exception = " + ex + ", status = " + status + "ErrorCode = " + errorCode);

		response.setStatus(status.value());
		response.setContentType("charset=utf-8");
		response.setCharacterEncoding("UTF-8");

		result.getHeader().setStatus(status.value());
		result.getBody().getDocs().setErrCode(errorCode);
		result.getBody().getDocs()
				.setErrMessage(messageSource.getMessage(errorCode, null, LocaleContextHolder.getLocale()));
		result.getBody().getDocs().addReturnMsg("stack", ex.getMessage());
		mav.addObject("error", result);
		mav.setViewName(JSP_VIEW_PATH+"error");

		return mav;
    }
}

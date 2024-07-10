package com.mindslab.web.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class MindsLabCustomFailureHandler implements AuthenticationFailureHandler {
	private final String DEFAULT_FAILURE_URL = "/index";// "/login?error=true";

	////////////////////////////////////////////////////////////////////////////////
	// < public functions (constructor)

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		boolean isError = false;
		String errorMessage = "";

		// =================================================
		// < set the error message
		// =================================================
		if (exception instanceof LockedException) {
			isError = true;
			errorMessage = "잘못된 비밀번호를 5회이상 입력하여 잠김처리 되었습니다.";
		} else if (exception instanceof BadCredentialsException
				|| exception instanceof UsernameNotFoundException
				|| exception instanceof InternalAuthenticationServiceException) {
			isError = true;
			errorMessage = "아이디나 비밀번호가 맞지 않습니다.";
		} else if (exception instanceof DisabledException) {
			isError = true;
			errorMessage = "계정이 비활성화 되었습니다.";
		} else if (exception instanceof CredentialsExpiredException) {
			isError = true;
			errorMessage = "비밀번호 유효기간이 만료 되었습니다.";
		} else if (exception instanceof CredentialsExpiredException) {
			isError = true;
			errorMessage = "비밀번호 유효기간이 만료 되었습니다.";
		} else if (exception instanceof MindsLabAuthenticationException) {
			MindsLabAuthenticationException mException = (MindsLabAuthenticationException) exception;
			isError = true;
			if (AuthErrorCode.INVALID_STATUS.getErrorCode().equals(mException.getErrorCode().getErrorCode())) {
				errorMessage = "아이디나 비밀번호가 맞지 않습니다.";
			} else {
				errorMessage = exception.getMessage();
			}
		} else {
			isError = true;
			errorMessage = "알수 없는 이유로 로그인에 실패하였습니다.";
		}

		// < set attributes
		request.setAttribute("isError", isError);
		request.setAttribute("errorMessage", errorMessage);
		// < redirection
		request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);
	}

	////////////////////////////////////////////////////////////////////////////////
	// < private functions
}
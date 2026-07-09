package com.mindslab.web.auth;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class MindsLabAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException{

		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/errors/entrypoint");
		dispatcher.forward(request, response);

	}
}

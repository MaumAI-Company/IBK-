package com.mindslab.web.auth;

import com.mindslab.web.common.error.CommonErrorCode;
import com.mindslab.web.vo.MemberVO;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MindsLabAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private AuthenticationManager authenticationManager;
	private MemberVO member;

	public MindsLabAuthenticationFilter(AuthenticationManager authenticationManager){
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException{

		// Grab credentials and map then to MemberVO
		try{
			member = new ObjectMapper().readValue(request.getInputStream(),MemberVO.class);
			log.info("attemptAuthentication: userId = {}" + member.getMemId());
		} catch (IOException e){
			throw new MindsLabAuthenticationException("Bad principals",
					CommonErrorCode.BAD_REQUEST);
		}
		// Create login token
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				member.getMemName(), member.getMemPwd(), new ArrayList<>());
		authenticationToken.setDetails(member);

		// Authenticate user
		Authentication auth = authenticationManager.authenticate(authenticationToken);

		return auth;
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException{
		if (failed instanceof MindsLabAuthenticationException){
			MindsLabAuthenticationException ex = (MindsLabAuthenticationException) failed;

			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/errors/auth-service-error/"
							+ ex.getErrorCode().getErrorCode() + "?lang="
							+ request.getParameter("lang"));
			dispatcher.forward(request, response);

		} else{
			super.unsuccessfulAuthentication(request, response, failed);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException{

		SecurityContextHolder.getContext().setAuthentication(authResult);
	}
}

package com.mindslab.web.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

public class MDCAuthenticationFilter extends OncePerRequestFilter {

	private static final String MDC_KEY = "userId";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails user = (UserDetails) authentication.getPrincipal();
			MDC.put(MDC_KEY, user.getUsername());
			try {
				filterChain.doFilter(request, response);
			} catch(ServletException e) {
				// nothing to do
				throw e;
			} catch(IOException e) {
				// nothing to do
				throw e;
			} finally {
				MDC.remove(MDC_KEY);
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}

}
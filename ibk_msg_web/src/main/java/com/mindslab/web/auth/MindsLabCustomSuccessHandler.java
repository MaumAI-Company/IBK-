package com.mindslab.web.auth;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mindslab.web.common.support.utils.RoleConst;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

@Component
public class MindsLabCustomSuccessHandler implements AuthenticationSuccessHandler {
	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();
	private final String DEFAULT_LOGIN_SUCCESS_URL = "/home";

	////////////////////////////////////////////////////////////////////////////////
	// < public functions (override)

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// < clear authentication error
		clearAuthenticationAttributes(request);
		// < redirect page
		redirectStrategy(request, response, authentication);
	}

	////////////////////////////////////////////////////////////////////////////////
	// < private functions

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}

	private void redirectStrategy(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// < get the saved request
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest == null) {
			redirectStratgy.sendRedirect(request, response, DEFAULT_LOGIN_SUCCESS_URL);
		} else {
			// < get the authorities
			Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
			if (roles.contains(RoleConst.ADMIN.getRole())) {
				redirectStratgy.sendRedirect(request, response, "/admin");
			} else if (roles.contains(RoleConst.USER.getRole())) {
				redirectStratgy.sendRedirect(request, response, "/user");
			} else {
				redirectStratgy.sendRedirect(request, response, "/mindslab/guest");
			}
		}
	}
}

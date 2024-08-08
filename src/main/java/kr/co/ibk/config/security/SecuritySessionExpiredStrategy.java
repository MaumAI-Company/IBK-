/************************************************************
 * 시스템 명 :
 * 업무명 :
 * 프로그램명(ID) :
 * 프로그램 설명 :
 *
 * 작성일 : 2018. 1. 19.
 * 작성자 : "fishingday"
 *
 * 수정자     수정일자     수정내역
 * ------    ----------    ---------------------------------
 * "fishingday"    2018. 1. 19.    최초 생성
 *
 ************************************************************/
package kr.co.ibk.config.security;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SecuritySessionExpiredStrategy.java
 * @author "fishingday"
 * @see
 */
@Component
public class SecuritySessionExpiredStrategy implements SessionInformationExpiredStrategy {

	private String defaultUrl;

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public SecuritySessionExpiredStrategy setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
		return this;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.session.SessionInformationExpiredStrategy#onExpiredSessionDetected(org.springframework.security.web.session.SessionInformationExpiredEvent)
	 */
	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		HttpServletResponse response = event.getResponse();
		response.sendRedirect(defaultUrl);
	}

}

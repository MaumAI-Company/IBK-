package com.mindslab.web.config;

import javax.servlet.ServletRequestEvent;
import javax.servlet.annotation.WebListener;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@WebListener
public class RequestContextListenerConfig extends RequestContextListener {

    @Override
	public void requestInitialized(ServletRequestEvent requestEvent){
		super.requestInitialized(requestEvent);

        String lang = requestEvent.getServletRequest().getParameter("lang");
        if (lang != null) {
            LocaleContextHolder.setLocale(StringUtils.parseLocaleString(lang));
        }
	}
}

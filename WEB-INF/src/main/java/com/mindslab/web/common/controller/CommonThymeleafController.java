package com.mindslab.web.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;

public class CommonThymeleafController {
    @Autowired
	@Qualifier("messageSource")
    MessageSource messageSource;
}

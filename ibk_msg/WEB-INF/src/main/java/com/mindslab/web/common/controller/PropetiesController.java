package com.mindslab.web.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest/props")
public class PropetiesController {
    @Autowired
    Environment environment;

	@RequestMapping("/{key}")
	@ResponseBody
	public String checkGetProperty(@PathVariable String key) {
		return environment.getProperty(key);
	}
}

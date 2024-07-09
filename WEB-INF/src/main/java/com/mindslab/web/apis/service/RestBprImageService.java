package com.mindslab.web.apis.service;

import java.util.Map;

import com.mindslab.web.common.support.utils.CustomMap;
import com.speno.apis.aConnect;

public interface RestBprImageService {

	public CustomMap getBprImagePath(Map<String, Object> reqParam);
}

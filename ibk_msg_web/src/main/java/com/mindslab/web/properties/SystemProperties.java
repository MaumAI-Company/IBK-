package com.mindslab.web.properties;
import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class SystemProperties{

	@Resource
	private Environment environment;

	/**
	 * local 환경 여부
	 *
	 * @return
	 */
	public boolean isLocal(){
		String[] activeProfiles = environment.getActiveProfiles();
		if (activeProfiles.length > 0 && "local".equals(activeProfiles[0])){
			return true;
		} else{
			return false;
		}
	}

	/**
	 * 개발 환경 여부
	 *
	 * @return
	 */
	public boolean isDev(){
		String[] activeProfiles = environment.getActiveProfiles();
		if (activeProfiles.length > 0 && "dev".equals(activeProfiles[0])){
			return true;
		} else{
			return false;
		}
	}

	/**
	 * real 환경 여부
	 *
	 * @return
	 */
	public boolean isReal(){
		String[] activeProfiles = environment.getActiveProfiles();
		if (activeProfiles.length > 0 && "real".equals(activeProfiles[0])){
			return true;
		} else{
			return false;
		}
	}
}

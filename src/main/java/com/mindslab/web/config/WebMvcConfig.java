package com.mindslab.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mindslab.web.properties.MindsLabProperties;
import com.mindslab.web.properties.SystemProperties;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer{
	@Autowired
	protected MindsLabProperties mindsLabProperties;
	
	@Autowired
	protected SystemProperties systemProperties;	
	
	@Override
	public void addCorsMappings(CorsRegistry registry){
		registry.addMapping("/**").allowedOrigins("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD",
						"OPTIONS")
				.allowCredentials(false)
				.allowedHeaders("Origin", "X-Requested-With", "Content-Type",
						"Accept", "Authorization")
				.maxAge(3600);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/static/**")
				.addResourceLocations("classpath:/static/");
		//jsp 정적리소스 위치
		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/");
		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/");
		registry.addResourceHandler("/font/**").addResourceLocations("/WEB-INF/font/");
		registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/");
		registry.addResourceHandler("/webfonts/**").addResourceLocations("/WEB-INF/webfonts/");
		registry.addResourceHandler("/sample/**").addResourceLocations("/WEB-INF/sample/");
		// bpr이미지는 서버의 물리경로에 위치하여야 하므로 수정 (local: 윈도우, dev : 도커, real : 도커)
		if (systemProperties.isLocal()) {
			registry.addResourceHandler("/bprImg/**").addResourceLocations("file:\\\\\\"+mindsLabProperties.getBprImagePath()+"/");
		} else if (systemProperties.isDev()) {
			registry.addResourceHandler("/bprImg/**").addResourceLocations("file:"+mindsLabProperties.getBprImagePath()+"/");
		} else if (systemProperties.isReal()) {
			registry.addResourceHandler("/bprImg/**").addResourceLocations("file:"+mindsLabProperties.getBprImagePath()+"/");
		}
		// 엑셀 파일 서버의 물리경로에 위치하여야 하므로 수정 (local: 윈도우, dev : 도커, real : 도커) learnExcelPath
		if (systemProperties.isLocal()) {
			registry.addResourceHandler("/learn/**").addResourceLocations("file:\\\\\\"+mindsLabProperties.getLearnExcelPath()+"/");
		} else if (systemProperties.isDev()) {
			registry.addResourceHandler("/learn/**").addResourceLocations("file:"+mindsLabProperties.getLearnExcelPath()+"/");
		} else if (systemProperties.isReal()) {
			registry.addResourceHandler("/learn/**").addResourceLocations("file:"+mindsLabProperties.getLearnExcelPath()+"/");
		}
	}
	
	// ADD START
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("UTF-8");
		commonsMultipartResolver.setMaxUploadSizePerFile(10 * 1024 * 1024); // 10 MB 제한
		return commonsMultipartResolver;
	}
	// ADD END
}

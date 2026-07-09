package kr.co.ibk.config;

import kr.co.ibk.common.filters.ConnectHisFilter;
import kr.co.ibk.common.intercepters.MenuAuthInterceptor;
import kr.co.ibk.service.CommonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String serviceUrl;
    private final String filepath;
    private final String filepathuri;
    private final CommonService commonService;

    public WebConfig(@Value("${Globals.fileStorePath}") String filepath,
                     @Value("${Globals.fileStoreUriPath}") String filepathuri,
                     @Value("${Globals.domain.full}") String serviceUrl,
                     CommonService commonService) {
        this.filepath = filepath;
        this.filepathuri = filepathuri;
        this.serviceUrl = serviceUrl;
        this.commonService = commonService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(menuAuthInterceptor())
                .addPathPatterns("/soulGod/**")
                .order(0);
    }

    @Bean
    MenuAuthInterceptor menuAuthInterceptor() {
        return new MenuAuthInterceptor(commonService);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(filepathuri + "/**")
                .addResourceLocations("file:///" + filepath + "/")
                .setCachePeriod(0);

    }

    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {

        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new ConnectHisFilter());
        registrationBean.addUrlPatterns("/login");

        return registrationBean;
    }
}

package com.mindslab.web.config;

import com.mindslab.web.auth.MindsLabAccessDeniedHandler;
import com.mindslab.web.auth.MindsLabAuthenticationEntryPoint;
import com.mindslab.web.auth.MindsLabAuthenticationFilter;
import com.mindslab.web.auth.MindsLabAuthenticationProvider;
import com.mindslab.web.auth.MindsLabCustomFailureHandler;
import com.mindslab.web.auth.MindsLabCustomSuccessHandler;
import com.mindslab.web.auth.MindsLabLogoutSuccessHandler;
import com.mindslab.web.auth.UserPrincipalDetailsService;
import com.mindslab.web.common.support.utils.RoleConst;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 로그인 처리 authenticationProvider UserDetailsService
    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;
    @Autowired
    private MindsLabAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private MindsLabAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private MindsLabLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private MindsLabCustomSuccessHandler successHandler;
    @Autowired
    private MindsLabCustomFailureHandler failureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 테스트를위해 inMemoryAuthentication인증방식을 사용하여 아이디/비밀번호/권한 직접 입력
        /*
         * auth.inMemoryAuthentication()
         * //기본 user
         * .withUser("user1").password(passwordEncoder().encode("user1")).roles("USER")
         * //기본 admin
         * .and().withUser("admin").password(passwordEncoder().encode("admin")).roles(
         * "USER").roles("ADMIN");
         */
        auth.authenticationProvider(authenticationProvider());

    }

    @Bean
    MindsLabAuthenticationProvider authenticationProvider() {
        MindsLabAuthenticationProvider authenticationProvider = new MindsLabAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

        return authenticationProvider;
    }

    @Override
    // .antMatchers("/css/**", "/js/**", "/img/**" ,
    // "/include/**","/mindslab/**").permitAll()
    public void configure(WebSecurity web) throws Exception {
        // web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 정적 리소스 시큐리티 허용
        http.authorizeRequests().antMatchers("/js/**", "/css/**", "/font/**", "/images/**", "/webfonts/**").permitAll();

        http.headers().frameOptions().disable();
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
                // servlet Filter before 시점
                .addFilterBefore(new MindsLabAuthenticationFilter(authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                // MDC 로그4j 적용
                .addFilterAfter(new MDCAuthenticationFilter(), MindsLabAuthenticationFilter.class)
                // 요청에 의한 보안검사 시작
                .authorizeRequests()
                // admin role 권한만 접근 가능
                .antMatchers("/admin/**").hasRole(RoleConst.ADMIN.name())
                .antMatchers("/management/**").hasAnyRole(RoleConst.ADMIN.name(), RoleConst.USER.name())
                // user role/admin 권한 만 접근가능
                .antMatchers("/user/**").hasAnyRole(RoleConst.ADMIN.name(), RoleConst.USER.name())
                .antMatchers("/sso/**").permitAll() // SSO는 권한 체크 X
                .antMatchers("/main/**").permitAll()
                .antMatchers("/errors/**").permitAll()
                .antMatchers("/login", "/mindslab/**/login").permitAll()
                .antMatchers("/mindslab/guest").permitAll()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/thymeleaf/**", "/static/**").permitAll()
                .antMatchers("/agentApi/**", "/rest/**").permitAll()

                // 어떤 요청에도 보안검사를 한다.
                .anyRequest().authenticated();
        // 보안 검증은 formLogin방식으로 하겠다.
        http.formLogin()
                // default : Spring Boot 기본 제공 /login 로그인페이지 변경
                .loginPage("/mindslab/login")
                // 로그인 처리 url
                .loginProcessingUrl("/mindslab/loginProcess")
                .usernameParameter("userId") // 로그인시 파라미터로 "userId"를 받습니다
                .passwordParameter("pwd") // 로그인시 파라미터로 "password"를 받습니다
                .defaultSuccessUrl("/home") // 항상 defaultSuccessUrl 타는 경우 ("/home", true)
                .failureUrl("/mindslab/login") // default : /login?error || /mindslab/login, /errors/CMMN-404
                .successHandler(successHandler)
                .failureHandler(failureHandler);

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/mindslab/logout")) // logout 경로로 요청이 들어올 경우 /로 리다이렉트
                                                                                     // 하고 세션 초기화
                // .logoutSuccessUrl("/") // /로 리다이렉트 하고
                // 로그아웃 성공 핸들링
                .logoutSuccessHandler(logoutSuccessHandler).permitAll()
                // 세션 초기화
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "memberCookie")
                // 보안 Exception 핸들링
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);
    }

    // lucy-xss-servlet 사용한 크로스 사이트 스크립팅 보안정책 처리 bean 추가, 추 후 cros 처리 url
    // lucy-xss-servlet-filter-rule.xml 참조
    @Bean
    public FilterRegistrationBean<XssEscapeServletFilter> getXssFilterRegistrationBean() {
        FilterRegistrationBean<XssEscapeServletFilter> registrationBean = new FilterRegistrationBean<XssEscapeServletFilter>();
        registrationBean.setFilter(new XssEscapeServletFilter());
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

}

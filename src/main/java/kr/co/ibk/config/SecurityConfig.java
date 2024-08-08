package kr.co.ibk.config;

import kr.co.ibk.common.handlers.CustomLoginFailureHandler;
import kr.co.ibk.common.handlers.CustomLoginSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ApplicationContext context;

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler("/loginSuccess");
    }

    @Bean
    public AuthenticationFailureHandler failureHandler(String userId, String userPw) {
        return new CustomLoginFailureHandler("/loginFailure", userId, userPw);
    }

    /**
     * resource/static 제외
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().mvcMatchers("/favicon.ico");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        //web.ignoring().antMatchers( "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CharacterEncodingFilter filter = new CharacterEncodingFilter();

        http.headers().frameOptions().disable();
        http.headers().xssProtection().block(true);
        http.headers().defaultsDisabled().contentTypeOptions();

        http.authorizeRequests()
                .mvcMatchers("/ClientUI/**","/node_modules/**","/loginFailure","/message","/error","/ssoLoginAction"
                        ,"/fragments/**","/api/v1/common/download","/","/index","/login","/healthcheck","/coding/**").permitAll()
                .mvcMatchers("/api/member/**","/api/openData/**").permitAll()
//                .mvcMatchers("/api/v1/**").hasAnyRole("USER, ADMIN")
//                .mvcMatchers("/pages/**").hasAnyRole("USER, ADMIN")
//                .mvcMatchers("/soulGod/**").hasAnyRole("ADMIN")
                .mvcMatchers("/**").permitAll() //개발 중 임시 오픈
                .anyRequest().authenticated()
        ;

        http.formLogin()
                .loginPage("/login").permitAll()
                //.successHandler(successHandler())
                .successForwardUrl("/loginSuccess")
                //.failureForwardUrl("/loginFailure")
                .usernameParameter("userId")                  // 폼 데이터중 로그인 아이디로 사용할 이름
                .passwordParameter("userPw")               // 폼 데이터중 로그인 패스워드로 사용할 이름
                .failureHandler(failureHandler("userId", "userPw"))
        ;
        http.rememberMe()
                .userDetailsService(userDetailsService)
                .key("remember-me-key");

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
                /*.and()
                    .apply(new SpringSocialConfigurer())*/

        //http.addFilterBefore(ajaxSessionCheckFilter(), AjaxSessionCheckFilter.class);

        http.addFilterBefore(filter, CsrfFilter.class)
                .csrf().disable();

        http.sessionManagement()
                .sessionFixation()
                .changeSessionId()
                //.invalidSessionUrl("/")
                .maximumSessions(100)
                .maxSessionsPreventsLogin(true) //false : 이전세션아웃, true : 이전세션점유
        ;

        //<input type="checkbo" name="remember-me" />Remember Me
        /*http.rememberMe()
                .userDetailsService(chargerService)
                .key("remember-me-sample");*/
        //Async 로 생성된 하위 쓰레드에도 principal 정보가 공유하도록 정의
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

    }
}

package com.mindslab.web.auth;
import java.security.PrivateKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mindslab.web.common.controller.CommonController;
import com.mindslab.web.config.EncryptConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MindsLabAuthenticationProvider implements AuthenticationProvider {
	
	private UserPrincipalDetailsService userDetailsService;

	public void setUserDetailsService(UserPrincipalDetailsService userDetailsService){
		this.userDetailsService = (UserPrincipalDetailsService) userDetailsService;
	}

	@Autowired 
	private EncryptConfig encryptConfig;

    //로그인 버튼을 누를 경우
    //실행 1
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		// RSA 복호화
		String idRSA = authentication.getName();
		String pwdRSA = authentication.getCredentials().toString();
		
		PrivateKey privateKey = (PrivateKey) CommonController.RSA_PRIVATE_KEY;// 복호화
		
		String id="";
		String pwd="";
		try {
			id = CommonController.decryptRsa(privateKey, idRSA);
			pwd =  CommonController.decryptRsa(privateKey, pwdRSA);
		} catch (Exception e) {
		}
        //로그인 인증유저 사용자 정보를 조회하는 서비스이다.
		UserPrincipal member = new UserPrincipal();
		//SecurityContextHolder.getContext().setAuthentication(authentication);
		/*
		log.info("is userId {}", id);
		log.info("is pwd {}", pwd);
		log.info("is pwd encrypt bcrypt{}", encryptConfig.passwordEncoder().encode(pwd));
		*/
		userDetailsService.testUserByUsername(id, pwd);
		member = (UserPrincipal) userDetailsService.loadUserByUsername(id);

		if ( member == null ){
			log.info("사용자 정보가 없습니다.");
			throw new UsernameNotFoundException(id);
		//}else if(member != null && ! member.getPassword().equals(pwd) ) {
		}else if(member != null && ! encryptConfig.passwordEncoder("bcrypt").matches(pwd, member.getPassword()) ) {
			log.info("비밀번호가 틀렸습니다.");
			userDetailsService.addPasswordFailCnt(id);// 실패카운트 증가
			throw new BadCredentialsException(id);
		}
		
		userDetailsService.resetPasswordFailCnt(id);// 로그인 실패카운트 초기화
        return new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
	}

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}


package com.mindslab.web.auth;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mindslab.web.common.error.CommonErrorCode;
import com.mindslab.web.common.error.MindsLabException;
import com.mindslab.web.common.support.utils.WebUtil;
import com.mindslab.web.mapper.maria.MariaMemberMapper;
import com.mindslab.web.vo.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserPrincipalDetailsService implements UserDetailsService {

	// 의존성 주입
	@Autowired
	MariaMemberMapper mariaMemberMapper;
	
    @Autowired
    private HttpServletRequest request;

	public void testUserByUsername(String id, String pwd) {
		log.info("loadUserByUserId = {}", id);
		log.info("loadUserByUserPwd = {}", pwd);
		//log.info("request.getHeader(\"X-Forwarded-For\") = {}", request.getHeader("X-Forwarded-For"));
		//log.info("request.getRemoteAddr() = {}", request.getRemoteAddr());
		//log.info("WebUtil.getClientIp(request) = {}", WebUtil.getClientIp(request));
	}

	@Override
	public UserDetails loadUserByUsername(String userId)
			throws UsernameNotFoundException, MindsLabAuthenticationException {
		if (userId != null) {
			log.info("loadUserByUserName = {}", userId);
		} else {
			log.info("loadUserByUserName is null ");
		}
		// 변수 초기화
		MemberVO memberVO = null;
		UserPrincipal userPrincipal = null;
		String status = "";

		try {
			status = "Y";
			// mapper 등록 유저 조회 이 후 return UserPrincipal(memberVO) 리턴
			memberVO = mariaMemberMapper.getAuthInfo(userId);
			userPrincipal = new UserPrincipal(memberVO);
			
			log.info("USER Client IP = {}", WebUtil.getClientIp(request));
			log.info("USER Permit IP List = {}", memberVO.getMemColumn1());
			log.info("USER Permit IP Check YN = {}", memberVO.getMemColumn2());
			
			// 접근 불가능한 IP 체크
			String ipArr = ""; // "127.0.0.1, 192.0.0.1, .....";
			String ipCheck = "";
			String clientIp = WebUtil.getClientIp(request);
			
			if(memberVO != null ) {
				ipArr = memberVO.getMemColumn1() == null ? "" : memberVO.getMemColumn1() ;
				ipCheck = memberVO.getMemColumn2();
				
				String[] division = ipArr.split(",");//접속가능 아이피(여러개) 분리
				boolean isAccess = false;

				if (ipCheck != null && "Y".equals(ipCheck)) {
					for(int i =0;i<division.length;i++){
						//log.info("division[i].trim() :: "+ division[i].trim());
						if(clientIp.equals(division[i].trim())){
							//log.info("Client IP permit OK");
							isAccess = true;
							break;
						}else {
							//log.info("Client IP permit NO");
						}
					}
					if (!isAccess) {
						throw new MindsLabException(AuthErrorCode.IP_ACCESS_DENIED, "허가되지 않은 접근 IP 주소입니다. (접속 IP :"+clientIp+")");
					}
				}
			}

			// 사용자가 없거나 조회되었지만 ID가 없는경우 에러처리
			if (memberVO == null || memberVO.getMemId() == null || memberVO.getMemId().length() == 0) {
				// log.error("loadUserByUsername: not found is minds lab user");
				throw new UsernameNotFoundException(userId);
			} else if (memberVO != null && memberVO.getMemFailCnt() >= 5) {
				// 사용자가 조회 되었지만 실패 카운트수가 5회 이상 넘어가는경우 로그인 불가처리 락 처리
				throw new LockedException(userId);
			}

		} catch (UsernameNotFoundException e) {
			log.error("loadUserByUsername: not found is minds lab user");
			throw new MindsLabAuthenticationException("status is not use in MindsLab", AuthErrorCode.INVALID_STATUS);
		} catch (LockedException e) {
			log.error("loadUserByUsername: LockedException");
			throw new MindsLabAuthenticationException("잘못된 비밀번호를 5회이상 입력하여 잠김처리 되었습니다.", AuthErrorCode.PASSWORD_FAILCOUNT);
		} catch (MindsLabException e) {
			log.error("loadUserByUsername: MindsLabException = " + e.toString());
			throw new MindsLabAuthenticationException(e.getMessage(),
					e.getErrorCode());
		} catch (Exception e) {
			log.error("loadUserByUsername: Exception = " + e.toString());
			throw new MindsLabAuthenticationException("Internal server error",
					CommonErrorCode.INTERNAL_SERVER_ERROR);
		}

		return userPrincipal;
	}
	
	/**
     * # 실패 카운트 증가.
     * @param userId
     * @return Map<String,String>
     **/
	public void addPasswordFailCnt(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		Long updateCnt = null;// 실행 건 수
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		//
		MemberVO paramVO = new MemberVO();
		paramVO.setMemId(id);
		
		// 0. 비밀번호 규칙 체크 진행 후 정상이라면 변경, 아니라면 return 처리
		updateCnt = mariaMemberMapper.addPasswordFailCnt(paramVO);
		
		// 변경 결과 확인
		if (updateCnt != null && updateCnt == 1L) {
			// 변경된 값이 null이 아니고 1개의 정보가 변경되었다면 정상처리
			status = "SUCCESS";
			msg = "정상적으로 변경되었습니다.";
		} else { 
			status = "FAIL";
			msg = "실패하였습니다.";
		}
		// 결과 데이터 입력 
		map.put("status", status);
		map.put("msg", msg);
		
		//return map;
		
	}
	
	/**
     * # 실패 카운트 증가.
     * @param userId
     * @return Map<String,String>
     **/
	public void resetPasswordFailCnt(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		Long updateCnt = null;// 실행 건 수
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		//
		MemberVO paramVO = new MemberVO();
		paramVO.setMemId(id);
		
		// 0. 비밀번호 규칙 체크 진행 후 정상이라면 변경, 아니라면 return 처리
		updateCnt = mariaMemberMapper.resetPasswordFailCnt(paramVO);
		
		// 변경 결과 확인
		if (updateCnt != null && updateCnt == 1L) {
			// 변경된 값이 null이 아니고 1개의 정보가 변경되었다면 정상처리
			status = "SUCCESS";
			msg = "정상적으로 변경되었습니다.";
		} else { 
			status = "FAIL";
			msg = "실패하였습니다.";
		}
		// 결과 데이터 입력 
		map.put("status", status);
		map.put("msg", msg);
		
		//return map;
		
	}
}

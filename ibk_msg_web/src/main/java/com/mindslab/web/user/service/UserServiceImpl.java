package com.mindslab.web.user.service;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.config.EncryptConfig;
import com.mindslab.web.mapper.maria.MariaAdminDeptMapper;
import com.mindslab.web.mapper.maria.MariaMemberMapper;
import com.mindslab.web.vo.DeptVO;
import com.mindslab.web.vo.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
	
	@Autowired
	private MariaMemberMapper mariaMemberMapper;
	
	@Autowired
	private MariaAdminDeptMapper mariaDeptMapper;
	
	@Autowired
	private EncryptConfig encryptConfig;

	@Override
	public HashMap<String, Object> getUserInfo(String userId){
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// 사용자의 ID 값을 받아서 사용자의 정보 조회 후 반환처리
		MemberVO memberVO = mariaMemberMapper.getUserInfo(userId); 
		
		// 사용자의 정보 중 DEPTID 값으로 부서정보 조회
		DeptVO params = new DeptVO(); 
		params.setDeptId(memberVO.getDeptId());
		DeptVO deptVO = mariaDeptMapper.getDeptInfo(params);

		map.put("memberVO", memberVO);
		map.put("deptVO", deptVO);

		return map;
	}
	
    /**
     * 
     * ! 비밀번호 변경 로직
     * 
     * @param String userId, String chkPwd, String newPwd
     * @return HashMap<String, Object>
     */
	@Override
	public HashMap<String, Object> changeUserPassword(String userId, String chkPwd, String newPwd){

		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		Long updateCnt = null;// 실행 건 수
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		
		// 기존 사용자 정보 Get
		MemberVO oldMemberVO = mariaMemberMapper.getUserInfo(userId);
		
		// 변경될 사용자 정보 Set
		MemberVO newMemberVO = new MemberVO();
		
		// bcrypt 암호화 적용
		// String encChkPwd = encryptConfig.passwordEncoder("bcrypt").encode(chkPwd);
		String encNewPwd = encryptConfig.passwordEncoder("bcrypt").encode(newPwd);
		
		String oldPwd = oldMemberVO.getMemPwd();
		
		// 사용자의 현재 비밀번호와 입력한 기존 비밀번호와 비교
		if (encryptConfig.passwordEncoder("bcrypt").matches(chkPwd, oldPwd)) {
			if ("pass".equals(isValidPassword(newPwd))) {
				// 입력된 비밀번호와 기존 비밀번호와 일치하는 경우
				// 1. 비밀번호 변경 정보 Set > RUN
				newMemberVO.setMemId(userId);// 사용자 ID SET
				newMemberVO.setMemBefoPwd(oldPwd);// 기존 비밀번호 -> 이전 비밀번호
				newMemberVO.setMemPwd(encNewPwd);// 새로 입력한 비밀번호 -> 현재 비밀번호
				
				// 0. 비밀번호 규칙 체크 진행 후 정상이라면 변경, 아니라면 return 처리
				updateCnt = mariaMemberMapper.setUserPassword(newMemberVO); 
				// 실패카운트 초기화
				mariaMemberMapper.resetPasswordFailCnt(newMemberVO); 
				
				// 변경 결과 확인
				if (updateCnt != null && updateCnt == 1L) {
					// 변경된 값이 null이 아니고 1개의 정보가 변경되었다면 정상처리
					status = "SUCCESS";
					msg = "정상적으로 변경되었습니다.";
				} else { 
					status = "FAIL";
					msg = "실패하였습니다.";
				}
			} else {
				// 패스워드 규칙에 어긋남 
				status = "VALID";
				msg = "패스워드 규칙에 맞지 않습니다.";
			}
		} else {
			// 입력된 비밀번호와 기존 비밀번호가 다른 경우 
			status = "FAIL";
			msg = "입력된 정보가 올바르지 않습니다.";
		}
		
		/**
		 * 추후 개선
		 * 로그인 변경시의 비밀번호가 이전 비밀번호와 동일한 경우 처리
		 * 로그인 실패 컬럼 초기화
		 */
		
		// 결과 데이터 입력 
		map.put("status", status);
		map.put("msg", msg);
		
		return map;
	}

    /**
     * 비밀번호 검증 메소드
     *
     * @param password 비밀번호 문자열
     * @return 오류 메시지
     */
    public static String isValidPassword(String password) {
      // 최소 8자, 최대 12자 상수 선언
      final int MIN = 8;
      final int MAX = 12;

      // 영어, 숫자, 특수문자 포함한 MIN to MAX 글자 정규식
      final String REGEX = 
        "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";
      // 하나 이상의 대문자, 소문자, 숫자, 특수문자를 포함한 정규식
      final String REGEX_ALL =
        "^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";
      
      // 3자리 연속 문자 정규식
      final String SAMEPT = "(\\w)\\1\\1";
      // 공백 문자 정규식
      final String BLANKPT = "(\\s)";
      
      // 정규식 검사객체
      Matcher matcher;

      // 공백 체크
      if (password == null || "".equals(password)) {
        return "Detected: No Password";
      }

      // ASCII 문자 비교를 위한 UpperCase
      String tmpPw = password.toUpperCase();
      // 문자열 길이
      int strLen = tmpPw.length();

      // 글자 길이 체크
      if (strLen > MAX || strLen < MIN) {
        return "Detected: Incorrect Length(Length: " + strLen + ")";
      }

      // 공백 체크
      matcher = Pattern.compile(BLANKPT).matcher(tmpPw);
      if (matcher.find()) {
        return "Detected: Blank";
      }

      // 비밀번호 정규식 체크
      matcher = Pattern.compile(REGEX_ALL).matcher(password);
      if (!matcher.find()) {
        return "Detected: Wrong Regex_All";
      }

      // 동일한 문자 3개 이상 체크
      /*
      matcher = Pattern.compile(SAMEPT).matcher(tmpPw);
      if (matcher.find()) {
        return "Detected: Same Word";
      } 
      */
      
      return "pass";
    }

	/**
     * # 실패 카운트 리셋
     * @param userId
     * @return Map<String,String>
     **/
	@Override
	public HashMap<String, Object> resetPasswordFailCnt(String userId) {

		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		Long updateCnt = null;// 실행 건 수
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		//
		MemberVO paramVO = new MemberVO();
		paramVO.setMemId(userId);
		
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
		
		return map;
	}

	/**
     * # 실패 카운트 증가.
     * @param userId
     * @return Map<String,String>
     **/
	@Override
	public HashMap<String, Object> addPasswordFailCnt(String userId) {

		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		Long updateCnt = null;// 실행 건 수
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		//
		MemberVO paramVO = new MemberVO();
		paramVO.setMemId(userId);
		
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
		
		return map;
	}
    
}

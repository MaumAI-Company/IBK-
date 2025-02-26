package kr.co.ibk.service;

import kr.co.ibk.config.EncryptConfig;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService extends _BaseService {

    private final EncryptConfig encryptConfig;

    private final MemberRepository memberRepository;

    /**
     * ! 비밀번호 변경 로직
     *
     * @param String userId, String chkPwd, String newPwd
     * @return HashMap<String, Object>
     */
    public HashMap<String, Object> changeUserPassword(String userId, String chkPwd, String newPwd) {

        HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
        Long updateCnt = null;// 실행 건 수
        String status = "ERROR"; // 상태
        String msg = "에러";// 메시지


        // 기존 사용자 정보 Get
        MemberInfo oldMember = memberRepository.getUserInfo(userId);

        // 변경될 사용자 정보 Set
        MemberInfo newMember = new MemberInfo();

        // bcrypt 암호화 적용
        // String encChkPwd = encryptConfig.passwordEncoder("bcrypt").encode(chkPwd);
        String encNewPwd = encryptConfig.passwordEncoder("bcrypt").encode(newPwd);

        String oldPwd = oldMember.getMemPwd();

        // 사용자의 현재 비밀번호와 입력한 기존 비밀번호와 비교
        if (encryptConfig.passwordEncoder("bcrypt").matches(chkPwd, oldPwd)) {
            newMember.setMemId(userId);// 사용자 ID SET
            newMember.setMemBefoPwd(oldPwd);// 기존 비밀번호 -> 이전 비밀번호
            newMember.setMemPwd(encNewPwd);// 새로 입력한 비밀번호 -> 현재 비밀번호

            // 0. 비밀번호 규칙 체크 진행 후 정상이라면 변경, 아니라면 return 처리
            updateCnt = memberRepository.setUserPassword(newMember);
            // 실패카운트 초기화
            memberRepository.resetPasswordFailCnt(newMember);

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
            status = "FAIL";
            msg = "입력된 정보가 올바르지 않습니다.";
        }
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

    public Boolean confirmOldPassword(String userId, String chkPwd) {
        MemberInfo oldMember = memberRepository.getUserInfo(userId);
        String oldPwd = oldMember.getMemPwd();
        return encryptConfig.passwordEncoder("bcrypt").matches(chkPwd, oldPwd);
    }

    public Boolean confirmNewPassword(String userId, String newPwd) {
        MemberInfo oldMember = memberRepository.getUserInfo(userId);
        String oldPwd = oldMember.getMemPwd();
        return !encryptConfig.passwordEncoder("bcrypt").matches(newPwd, oldPwd);
    }
}

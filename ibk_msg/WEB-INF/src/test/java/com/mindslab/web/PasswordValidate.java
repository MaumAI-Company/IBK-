package com.mindslab.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mindslab.WebApplication;
import com.mindslab.web.config.EncryptConfig;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = WebApplication.class, properties = "spring.profiles.active:local")
@ActiveProfiles("local")
@Slf4j
public class PasswordValidate {

    @Autowired
    private EncryptConfig encryptConfig;

    @Test
    public void encode() throws Exception {// 암호화 테스트
    	 // Test Set
        String[] testSet = {
            "ibk!1234"
    		, "votmdnj&em123"
            , "kjs@aldkjfklj43"
            , "QBWfklj4543"
            , "abct438983"
            , "acdf@sabcer9182"
            , "alfl234kdd"
            , "asd@fasdf987"
            // Blank 테스트 문자열
            , "xp@tmxm85 84"
            // 공백 테스트 문자열
            , ""
            // 문자 길이 테스트 문자열
            , "OJHDSJK@HFzDLKDJLJoiejwf42^%wij"
            , "xyz47@"
            , "1lkjvneim@"
            // ASCII Overflow 테스트 문자열
            , "/01alkjdffn"
            , "9:;aslkdjfkja2"
            , "?@alakjlkiie3"
            , "Z[\\ekjmvkfd4"
            , "@abieofinv2"
            , "89:8973589723dfasb"
            , "YZ[qoeirnvk235"
        };

        for (String s : testSet) {
          System.out.println("Password: \"" + s + "\"");
          System.out.println(isValidPassword(s));
          System.out.println("--------------------------------");
        }
    }
    /**
     * 비밀번호 검증 메소드
     *
     * @param password 비밀번호 문자열
     * @return 오류 메시지
     */
    public static String isValidPassword(String password) {
      // 최소 8자, 최대 20자 상수 선언
      final int MIN = 8;
      final int MAX = 8;

      // 영어, 숫자, 특수문자 포함한 MIN to MAX 글자 정규식
      final String REGEX = 
        "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";
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
      if (strLen > 20 || strLen < 8) {
        return "Detected: Incorrect Length(Length: " + strLen + ")";
      }

      // 공백 체크
      matcher = Pattern.compile(BLANKPT).matcher(tmpPw);
      if (matcher.find()) {
        return "Detected: Blank";
      }

      // 비밀번호 정규식 체크
      matcher = Pattern.compile(REGEX).matcher(tmpPw);
      if (!matcher.find()) {
        return "Detected: Wrong Regex";
      }

      // 동일한 문자 3개 이상 체크
      matcher = Pattern.compile(SAMEPT).matcher(tmpPw);
      if (matcher.find()) {
        return "Detected: Same Word";
      } 
      return "pass";
    }
}

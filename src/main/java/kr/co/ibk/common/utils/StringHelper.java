package kr.co.ibk.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringHelper {

    public static String getClientIP(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Deprecated
    public static String phoneNumDelete(String str) {
    	return MaskHelper.phoneNum(str);
    }

    public static String birthYear(String str) {

        String replaceString = "";
        if (str != null && !str.equalsIgnoreCase("")) {
            replaceString = str;

            Matcher matcher = Pattern.compile("^(\\d{4})$").matcher(str);

            if (matcher.matches()) {
                replaceString = "";

            /*boolean isHyphen = false;
            if(str.indexOf("-") > -1) {
                isHyphen = true;
            }*/

                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String replaceTarget = matcher.group(i);
                    if (i == 2) {
                        char[] c = new char[replaceTarget.length()];
                        Arrays.fill(c, '*');

                        replaceString = replaceString + String.valueOf(c);
                    } else {
                        replaceString = replaceString + replaceTarget;
                    }

                /*if(isHyphen && i < matcher.groupCount()) {
                    replaceString = replaceString + "-";
                }*/
                }
            }
        }

        return replaceString;
    }


    @Deprecated
    public static String nameDelete(String str) {
    	return MaskHelper.name(str);
    }

    @Deprecated
    public static String emailDelete(String str) {
        
        return MaskHelper.email(str); 
    }

    @Deprecated
    public static String ipDelete(String str) {
        return MaskHelper.ip(str);
    }

    public static String onlyNumber(String str) {
        String replaceString = "";
        if (str != null && !str.equalsIgnoreCase("")) {
            replaceString = str.replaceAll("\\D", "");
        }
        return replaceString;
    }

    public static String maskingName(String name) {
        if (name == null || name.length() < 2) return name;

        String rtnNm = "";
        for (int i=0; i< name.length(); i++) {
            if ((i == 0) || (i == (name.length()-1))) {
                rtnNm += name.charAt(i);
            } else {
                rtnNm += "*";
            }
        }
        return rtnNm;
    }

    public static String removeTag(String str) {
        if (str == null) return null;
        return str.replaceAll("\\<.*?>","");
    }
    
    //대소문자 구분없이 text문자열에 checkText 존재하는지 리턴함. 
    public static boolean isMatchesIgnore(String text, String checkText) {
		if ( null == text || null == checkText ) return false; 
		
		//대소문자 구분없이 검색 
		String regex = "(?i).*" + checkText + ".*";
		
		return text.matches(regex); 
		
    }

    /**
     * 숫자에 천단위마다 콤마 넣기
     * @param num
     * @return String
     * */
    public static String toNumFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }

    public static String toNumFormat(Long num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }

    public static int birthYearToAge(String birthYear) {
        if (birthYear == null || "".equals(birthYear)) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int userYear = Integer.parseInt(birthYear);

        return (nowYear - userYear + 1);
    }

    public static String enterToBr(String text) {
        if (text == null) return null;
        return text.replaceAll("\n", "<br/>");
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

    public static String substringForNvarcharMax(String str) {
        String retStr = "";
        if (Objects.isNull(str) || str.isEmpty()) {
            return retStr;
        } else if (str.length() < 4000) {
            retStr = str;
            return retStr;
        }

        retStr = str.substring(0, 3999);

        return retStr;
    }

    /**
     * 임시 비밀번호
     * @return
     */
    public static String excuteGeneratePwd() {
        char[] passwordTable1 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z'};
        char[] passwordTable2 = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        char[] passwordTable3 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        Random random = new Random(System.currentTimeMillis());
        int tablelength1 = passwordTable1.length;
        int tablelength2 = passwordTable2.length;
        int tablelength3 = passwordTable3.length;
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < 5; i++) {
            buf.append(passwordTable1[random.nextInt(tablelength1)]);
        }
        for (int i = 0; i < 1; i++) {
            buf.append(passwordTable2[random.nextInt(tablelength2)]);
        }
        for (int i = 0; i < 2; i++) {
            buf.append(passwordTable3[random.nextInt(tablelength3)]);
        }

        return buf.toString();
    }


    /**
     * 휴대전화번호 010-1234-1234 형식
     *
     * @param str
     * @return
     */
    public static String phoneNumDashFormat(String str) {
        if (NullHelper.isEmpty(str)) {
            return "";
        }
        if (str.length() == 8) {
            return str.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (str.length() == 12) {
            return str.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return str.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }
}

package kr.co.ibk.common.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskHelper {

    /**
     * 핸드폰번호 마스킹처리.
     * @param str
     * @return
     */
    public static String phoneNum(String str) {
        String replaceString = "";
        if (str != null && !str.equalsIgnoreCase("")) {
            replaceString = str;

            Matcher matcher = Pattern.compile("^(\\d{3})-?(\\d{3,4})-?(\\d{4})$").matcher(str);

            if (matcher.matches()) {
                replaceString = "";

                boolean isHyphen = false;
                if (str.indexOf("-") > -1) {
                    isHyphen = true;
                }

                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String replaceTarget = matcher.group(i);
                    if (i == 2) {
                        char[] c = new char[replaceTarget.length()];
                        Arrays.fill(c, '*');

                        replaceString = replaceString + String.valueOf(c);
                    } else {
                        replaceString = replaceString + replaceTarget;
                    }

                    if (isHyphen && i < matcher.groupCount()) {
                        replaceString = replaceString + "-";
                    }
                }
            }
        }
        return replaceString;
    }

    /**
     * 핸드폰번호 뒷자리 마스킹처리.
     * @param str
     * @return
     */
    public static String phoneNumBack4(String str) {
        String replaceString = "";
        if (str != null && !str.equalsIgnoreCase("")) {
            replaceString = str;

            Matcher matcher = Pattern.compile("^(\\d{3})-?(\\d{3,4})-?(\\d{4})$").matcher(str);

            if (matcher.matches()) {
                replaceString = "";

                boolean isHyphen = false;
                if (str.indexOf("-") > -1) {
                    isHyphen = true;
                }

                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String replaceTarget = matcher.group(i);
                    if (i == 3) {
                        char[] c = new char[replaceTarget.length()];
                        Arrays.fill(c, '*');

                        replaceString = replaceString + String.valueOf(c);
                    } else {
                        replaceString = replaceString + replaceTarget;
                    }

                    if (isHyphen && i < matcher.groupCount()) {
                        replaceString = replaceString + "-";
                    }
                }
            }
        }
        return replaceString;
    }

    /**
     * 핸드폰번호 2자리만 마스킹 처리함. 
     * @param str
     * @return
     */
    public static String phoneNum2(String str) {

        if (str == null || str.isEmpty()) return str;

        //7자리보다 작으면 그대로 리턴.
        int minLength = 7;
        if (str.length() < minLength) return str;

        //7자리이상이면 6,7 자리는 마스킹, 나머지는 그대로.
        String phone = str.substring(0, minLength - 2);
        phone += "**"; //마스킹 2자리

        if (str.length() > minLength) {
            phone += str.substring(minLength);
        }

        return phone;
    }

    /**
     * 이메일주소 마스킹처리.
     * @param str
     * @return
     */
    public static String email(String str) {
        String replaceString = "";
        if (str != null && !str.equalsIgnoreCase("")) {
            replaceString = str;

            Matcher matcher = Pattern.compile("^(..)(.*)([@]{1})(.*)$").matcher(str);

            if (matcher.matches()) {
                replaceString = "";

                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String replaceTarget = matcher.group(i);
                    if (i == 2) {
                        char[] c = new char[replaceTarget.length()];
                        Arrays.fill(c, '*');

                        replaceString = replaceString + String.valueOf(c);
                    } else {
                        replaceString = replaceString + replaceTarget;
                    }
                }

            }
        }
        return replaceString;
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


    public static String name(String str) {

        String replaceString = "";
        if (str != null && !str.equalsIgnoreCase("")) {
            replaceString = str;

            String pattern = "";
            if (str.length() == 2) {
                pattern = "^(.)(.+)$";
            } else {
                pattern = "^(.)(.+)(.)$";
            }

            Matcher matcher = Pattern.compile(pattern).matcher(str);

            if (matcher.matches()) {
                replaceString = "";

                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String replaceTarget = matcher.group(i);
                    if (i == 2) {
                        char[] c = new char[replaceTarget.length()];
                        Arrays.fill(c, '*');

                        replaceString = replaceString + String.valueOf(c);
                    } else {
                        replaceString = replaceString + replaceTarget;
                    }

                }
            }
        }
        return replaceString;
    }

    /**
     * ip주소 마스킹.
     * @param str
     * @return
     */
    public static String ip(String str) {
        String replaceString = "";
        if (str != null && !str.equalsIgnoreCase("")) {
            replaceString = str;

            Matcher matcher = Pattern.compile("^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$").matcher(str);

            if (matcher.matches()) {
                replaceString = "";

                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String replaceTarget = matcher.group(i);
                    if (i == 3) {
                        char[] c = new char[replaceTarget.length()];
                        Arrays.fill(c, '*');

                        replaceString = replaceString + String.valueOf(c);
                    } else {
                        replaceString = replaceString + replaceTarget;
                    }
                    if (i < matcher.groupCount()) {
                        replaceString = replaceString + ".";
                    }
                }
            }
        }
        return replaceString;
    }

    /**
     * email 함수는 마스킹처리가 너무 많아 끝자리 2자리만 마스킹 하는 함수 추가. 2020.02.19
     * @param mail
     * @return
     */
    public static String email2(String mail) {
        if (mail == null) return "";
        if (mail.isEmpty()) return mail;

        String[] sp = mail.split("@");
        if (sp.length < 2) return mail;

        String first = sp[0];
        if (first.length() < 2) first = "*";
        else if (first.length() == 2) first = first.substring(0, 1) + "*";
        else if (first.length() == 3) first = first.substring(0, 2) + "*";
        else first = first.substring(0, first.length() - 2) + "**";

        String email = first + "@";
        //첫번째는 마스킹 했으니 2번째 부터 붙이기.
        for (int i = 1; i < sp.length; i++) {
            email += sp[i];
        }

        return email;
    }

    /**
     * 계좌번호 마스킹 처리
     * - 당행: 고객번호 마지막 2자리와 일련번호 마지막 3자리를 마스킹 처리한다.
     *   예시: 122-123456-01-123 -> 122-1234**-01-***
     * - 타행: 10번째 자리부터 이후 자리를 마스킹 처리한다.
     */
    public static String accountNumber(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // 숫자로 시작하고 숫자/하이픈으로 구성되며 숫자로 끝나는 패턴
        Pattern tokenPattern = Pattern.compile("\\b[0-9][0-9\\-]*[0-9]\\b");
        Matcher matcher = tokenPattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String token = matcher.group();
            String masked = maskAccountToken(token);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(masked));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private static String maskAccountToken(String token) {
        // 숫자만 추출 (하이픈 제거)
        String digits = token.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            return token;
        }

        boolean[] mask = new boolean[digits.length()];

        // 당행 처리 (003으로 시작하는 경우)
        if (digits.startsWith("003")) {

            // 고객번호 시작 인덱스 (은행코드 3자리 이후)
            int customerStart = 3;

            // 고객번호 최대 6자리
            int customerLen = Math.min(6, Math.max(0, digits.length() - customerStart));

            // 고객번호 마지막 2자리 마스킹
            int customerMaskStart = Math.max(customerStart, customerStart + customerLen - 2);
            for (int i = customerMaskStart; i < customerStart + customerLen; i++) {
                mask[i] = true;
            }

            // 지점코드 2자리
            int branchStart = customerStart + customerLen;
            int branchLen = Math.min(2, Math.max(0, digits.length() - branchStart));

            // 지점 이후(일련번호 전체) 마스킹
            int restStart = branchStart + branchLen;
            for (int i = restStart; i < digits.length(); i++) {
                mask[i] = true;
            }

        } else {
            // 타행 처리
            // 10번째 자리부터 전부 마스킹 (index 9부터)
            for (int i = 9; i < digits.length(); i++) {
                mask[i] = true;
            }
        }

        return rebuildWithMask(token, digits, mask);
    }

    /**
     * 원본 계좌번호 형식을 유지하면서
     * 숫자 자리만 마스킹 적용하여 재구성하는 메서드
     *
     * - 하이픈(-)은 그대로 유지
     * - 마스킹된 숫자는 '*'로 변환
     */
    private static String rebuildWithMask(String token, String digits, boolean[] mask) {
        StringBuilder result = new StringBuilder(token.length());
        int digitIndex = 0;
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            // 숫자인 경우
            if (Character.isDigit(c)) {

                char next = digits.charAt(digitIndex);

                // 마스킹 대상이면 '*'로 변경
                if (mask[digitIndex]) {
                    next = '*';
                }

                result.append(next);
                digitIndex++;

            } else {
                // 하이픈 등은 그대로 유지
                result.append(c);
            }
        }

        while (digitIndex < digits.length()) {
            result.append(mask[digitIndex] ? '*' : digits.charAt(digitIndex));
            digitIndex++;
        }

        return result.toString();
    }

}

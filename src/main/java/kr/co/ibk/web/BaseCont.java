package kr.co.ibk.web;

import kr.co.ibk.common.AppWebResult;
import kr.co.ibk.common.Base;
import kr.co.ibk.common.utils.RequestUtil;
import org.springframework.ui.Model;

import java.util.HashMap;


public abstract class BaseCont extends Base {


    /**
     * client(thymeleaf)에서 사용될 수 있도록 (공통)메세지를 기록함.
     *
     * @param model
     * @param message
     * @param success
     */
    private void setAppWebResult(Model model, String message, boolean success) {
        AppWebResult result = new AppWebResult(message);

        model.addAttribute(RequestUtil.getAppWebResultKey(), result);   // key  _app_web_result_
    }

    /**
     * 스크립트 입력방지(문자열 치환)
     * load 시 사용
     *
     * @param str
     * @return
     */
    public static String toReplace(String str) {
        if (str == null) {
            return null;
        }
        String returnStr = str;
        returnStr = returnStr.replaceAll("<br>", "\n");
        returnStr = returnStr.replaceAll("&gt;", ">");
        returnStr = returnStr.replaceAll("&lt;", "<");
        returnStr = returnStr.replaceAll("&quot;", "");
        returnStr = returnStr.replaceAll("&nbsp;", " ");
        returnStr = returnStr.replaceAll("&amp;", "&");
        return returnStr;
    }

    /**
     * 스크립트 입력 방지(문자열 치환)
     * save 시 사용
     *
     * @param srcString
     * @return
     */
    public static String getReplace(String srcString) {
        String rtnStr = null;
        try {
            StringBuffer strTxt = new StringBuffer("");
            char chrBuff;
            int len = srcString.length();
            for (int i = 0; i < len; i++) {
                chrBuff = (char) srcString.charAt(i);
                switch (chrBuff) {
                    case '<':
                        strTxt.append("&lt;");
                        break;
                    case '>':
                        strTxt.append("&gt;");
                        break;
                    case '&':
                        strTxt.append("&amp;");
                        break;
                    default:
                        strTxt.append(chrBuff);
                }
            }
            rtnStr = strTxt.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnStr;
    }

    public HashMap<String, String> jsonToHashMap(String json) {
        // JSON 문자열을 HashMap으로 변환
        HashMap<String, String> map = new HashMap<>();

        //외부 대괄호 제거
        json = json.substring(1, json.length() - 1);

        String[] pairs = json.split("\\],\\["); // ],[ 기준으로 나누기
        for (String pair : pairs) {
            // 대괄호와 큰따옴표 제거
            pair = pair.replaceAll("[\\[\\]\"]", "");
            String[] entry = pair.split(",", 2); // 첫 쉼표를 기준으로 나눔
            if (entry.length == 2) {
                map.put(entry[0].trim(), entry[1].trim());
            }
        }
        return map;
    }

}

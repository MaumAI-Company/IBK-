package kr.co.ibk.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.ibk.common.AppWebResult;
import kr.co.ibk.common.Base;
import kr.co.ibk.common.utils.RequestUtil;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;


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
        HashMap<String, String> map = new HashMap<>();

        try {
            // JSON 문자열을 HashMap으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            List<List<Object>> list = objectMapper.readValue(json, List.class);

            for (List<Object> entry : list) {
                String key = (String) entry.get(0);
                Object value = entry.get(1);

                // searchType 내부 리스트를 String으로 변환하여 저장
                if (value instanceof List) {
                    List<List<String>> subList = (List<List<String>>) value;
                    for (List<String> subEntry : subList) {
                        map.put(subEntry.get(0), subEntry.get(1));
                    }
                } else {
                    map.put(key, value.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }


}

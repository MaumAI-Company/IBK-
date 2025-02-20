package kr.co.ibk.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.ibk.common.AppWebResult;
import kr.co.ibk.common.Base;
import kr.co.ibk.common.utils.RequestUtil;
import kr.co.ibk.domain.web.TemplateInputInfo;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public String hashMapToJson(HashMap<String, String> map) {
        List<List<String>> listFormat = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = "";

        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                List<String> pair = new ArrayList<>();
                pair.add(entry.getKey());
                pair.add(entry.getValue());
                listFormat.add(pair);
            }
            result = objectMapper.writeValueAsString(listFormat);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public List<Map<String, Object>> templateInputInfoToMap(List<TemplateInputInfo> list) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (TemplateInputInfo info : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", info.getColName());
            map.put("sno", info.getSno());
            mapList.add(map);
        }
        return mapList;
    }



    /**
     * 검색어 파싱
     * @param map
     * @param type 컬럼명 replace 1:BC카드 조회, 2:..., 3:...
     * @return
     */
    protected String makeSearchQuery(Map<String, String> map, int type) {
        if (map == null) return null;
        List<String> conList = new ArrayList<>();
        AtomicBoolean isCon = new AtomicBoolean(false);
        try {
            map.forEach((s, s2) -> {
                StringBuilder tempQuery = new StringBuilder();
                String col = s;
                if (type == 1) {
                    if ("BDMN_ITEX_MNGM_NO".equals(col)) {
                        col = "co.BDGT_BSNS_FRCS_CON";
                    }
                    if (!"BDMN_ITEX_MNGM_NO".equals(col) && !"BDGT_PRFR_RSN_FRCS_CON".equals(col) && !"BDGT_BSNS_FRCS_CON".equals(col)) {
                        col = "ci." + col;
                    }
                    if (!"BDMN_ITEX_MNGM_NO".equals(col) && ("BDGT_PRFR_RSN_FRCS_CON".equals(col) || "BDGT_BSNS_FRCS_CON".equals(col))) {
                        col = "co." + col;
                    }
                }
                if (s2.contains("&&") || s2.contains("||") || s2.contains("!")) {
                    isCon.set(true);
                    String tempS = s2.trim();
                    while(!tempS.isEmpty()) {
                        String patternStr = "&&|\\|\\|";
                        Pattern pattern = Pattern.compile(patternStr);
                        Matcher matcher = pattern.matcher(tempS);
                        String value = "";
                        String oper = "";
                        if (matcher.find()) {
                            value = tempS.substring(0, matcher.start()).trim();
                            oper = tempS.substring(matcher.start(), matcher.end());
                            tempS = tempS.substring(matcher.end());
                        } else {
                            value = tempS;
                            tempS = "";
                        }

                        if (value.startsWith("!") && value.length() > 1) {
                            tempQuery.append(col).append(" NOT LIKE CONCAT('%', '").append(value.substring(1)).append("', '%')");
                        } else {
                            tempQuery.append(col).append(" LIKE CONCAT('%', '").append(value).append("', '%')");
                        }
                        if ("&&".equals(oper)) {
                            tempQuery.append(" AND ");
                        } else if ("||".equals(oper)) {
                            tempQuery.append(" OR ");
                        }
//                        System.out.println(value);
//                        System.out.println(oper);
//                        System.out.println(tempQuery);
                    }
                } else {
                    tempQuery = new StringBuilder(col + " LIKE CONCAT('%', '" + s2 + "', '%')");
                }
                conList.add(tempQuery.toString());
            });

            StringBuilder rstQuery = new StringBuilder();
            boolean isFirst = true;
            for (String s : conList) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    rstQuery.append(" AND ");
                }
                rstQuery.append("(").append(s).append(")");
            }

            System.out.println(rstQuery);

            return isCon.get() ? rstQuery.toString() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.mindslab.web.common.support.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CommonUtil {
	
    /**
     * @param JSONObject
     * @apiNote JSONObject를 Map<String, String> 형식으로 변환처리.
     * @return Map<String,String>
     **/
	/*
    public Map<String, Object> getMapFromJsonObject(JSONObject jsonObject) {
        Map<String, Object> map = null;

        try {
            map = new ObjectMapper().readValue(jsonObject.toString(), Map.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
	 */
	
    /**
     * @param Map<String, Object>
     * @apiNote Map<String, Object>를 JSONObject로 변환처리.
     * @return JSONObject
     **/
	/*
    public JSONObject convertMapToJson(Map<String, Object> map) {

        JSONObject json = new JSONObject();
        String key = "";
        Object value = null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            try {
                json.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }
    */
}

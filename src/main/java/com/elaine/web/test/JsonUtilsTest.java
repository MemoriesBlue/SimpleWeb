package com.elaine.web.test;

import com.elaine.web.utils.JsonUtils;

import java.util.Map;

/**
 * Created by jianlan on 15-9-13.
 */
public class JsonUtilsTest {
    public static void main(String[] args) throws Exception {
        String jsonStr = "{\"name\":\"lalasala\", \"age\":23, \"attr\": {\"isVip\":true}}";
        Map<String, Object> jsonObj = (Map<String, Object>)JsonUtils.parse(jsonStr);
        for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + "  type:" + entry.getValue().getClass());
        }
    }
}

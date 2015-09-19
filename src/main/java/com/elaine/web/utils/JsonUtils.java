package com.elaine.web.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jianlan on 15-8-9.
 */
public class JsonUtils {
    public static Object parse(String jsonStr) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(jsonStr);
        JsonToken token = parser.nextToken();
        if (token == JsonToken.START_OBJECT) {
            return parseObject(parser);
        }
        if (token == JsonToken.START_ARRAY) {
            return parseArray(parser);
        }
        return null;
    }
    private static Map<String, Object> parseObject(JsonParser parser) throws Exception {
        Map<String, Object> obj = new HashMap<String, Object>();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String name = parser.getCurrentName();
            JsonToken token = parser.getCurrentToken();
            Object value = null;
            if (token == JsonToken.START_OBJECT) {
                value = parseObject(parser);
            } else if (token == JsonToken.START_ARRAY) {
                value = parseArray(parser);
            } else if (token.isBoolean()) {
                value = parser.getBooleanValue();
            } else if (token.isNumeric()) {
                value = parser.getDecimalValue();
            } else {
                value = parser.getValueAsString();
            }
            obj.put(name, value);
        }
        return obj;
    }
    private static List<Object> parseArray(JsonParser parser) throws Exception {
        List<Object> res = Lists.newArrayList();
        while ( parser.nextToken() != JsonToken.END_ARRAY) {
            JsonToken token = parser.getCurrentToken();
            Object obj = null;
            if (token == JsonToken.START_OBJECT) {
                obj = parseObject(parser);
            }
            if (token == JsonToken.START_ARRAY) {
                obj = parseArray(parser);
            }
            if (token.isBoolean()) {
                obj = parser.getBooleanValue();
            }
            if (token.isNumeric()) {
                obj = parser.getDecimalValue();
            }
            res.add(obj);
        }
        return res;
    }
}

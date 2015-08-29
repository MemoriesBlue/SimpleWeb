package test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jianlan on 15-8-1.
 */
public class JacksonTest {
    public static Object parse(JsonParser parser) throws Exception {
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
            }
            if (token == JsonToken.START_ARRAY) {
                value = parseArray(parser);
            }
            if (token.isBoolean()) {
                value = parser.getBooleanValue();
            }
            if (token.isNumeric()) {
                value = parser.getDecimalValue();
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

    public static void main1(String[] args) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("{\"tmp1\":{\"id\":1, \"attr\":[1,2,4]}, \"tmp2\":{\"id\":2,\"attr\":[5,7,9]}}");
        parser.nextToken();
        /**
         * JsonToken.END_OBJECT:只是表示读到'}'符号,而JsonToken.START_OBJECT只是代表着'{'
         * JsonToken.END_ARRAY:只是表示读到']'符号,而JsonToken.START_ARRAY只是代表着'['
         */
        while(parser.nextToken() != JsonToken.END_OBJECT) {
            String name = parser.getCurrentName();
            if ("tmp1".equals(name)) {
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    if ("id".equals(parser.getCurrentName())) {
                        JsonToken jsonToken = parser.nextToken();
                        System.out.println(jsonToken.isNumeric());
                        System.out.println(parser.getDecimalValue());
                        System.out.println(parser.getCurrentName());
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("{\"tmp1\":{\"id\":1, \"attr\":[1,2,4]}, \"tmp2\":{\"id\":2,\"attr\":[5,7,9]}}");
        Object res = parse(parser);
        if (res == null) {
            System.out.println("null");
        }
        if (res instanceof Map) {
            System.out.println("map");
            Map<String, Object> resMap = (Map<String, Object>)res;
            Object tmp1 = resMap.get("tmp1");
            if (tmp1 != null) {
                Map<String, Object> tmp1Map = (Map<String, Object>) tmp1;
                System.out.println(tmp1Map.get("id"));
                List<Object> attr = (List<Object>)tmp1Map.get("attr");
                for (Object o : attr) {
                    System.out.println(o);
                }
            }

            Object tmp2 = resMap.get("tmp2");
            if (tmp2 != null) {
                Map<String, Object> tmp2Map = (Map<String, Object>) tmp2;
                System.out.println(tmp2Map.get("id"));
                List<Object> attr = (List<Object>)tmp2Map.get("attr");
                for (Object o : attr) {
                    System.out.println(o);
                }
            }

        }
        if (res instanceof List) {
            System.out.println("list");
        }
    }
}

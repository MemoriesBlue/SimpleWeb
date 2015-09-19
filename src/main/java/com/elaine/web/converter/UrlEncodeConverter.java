package com.elaine.web.converter;

import com.elaine.web.model.Request;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by jianlan on 15-9-4.
 */
public class UrlEncodeConverter extends HttpInputConverter {
    public Request convert(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paraMaps = Maps.newHashMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            Object newValue = value[0];
            if (value.length > 1) {
                newValue = Lists.newArrayList(value);
            }
            paraMaps.put(key, newValue);
        }
        Request req = new Request();
        req.setParaMaps(paraMaps);
        req.setContentType(parseContentType(request));
        req.setEncoding(parseEncoding(request));
        req.setRequestPattern(buildRequestPattern(request));
        return req;
    }
}

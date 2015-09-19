package com.elaine.web.converter;

import com.elaine.web.model.Request;
import com.elaine.web.utils.JsonUtils;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by jianlan on 15-9-4.
 */
public class HttpJsonConverter extends HttpInputConverter {
    private final String DEFAULT_ENCODE = "utf8";
    public Request convert(HttpServletRequest request) {
        Map<String, Object> paraMaps = Maps.newHashMap();
        try {
            String encoding = parseEncoding(request);
            if (StringUtils.isBlank(encoding)) {
                encoding = DEFAULT_ENCODE;
            }
            String jsonStr = IOUtils.toString(request.getInputStream(), encoding);
            Object parseObj = JsonUtils.parse(jsonStr);
            if (!(parseObj instanceof Map)) {
                throw new RuntimeException("json不是一个Map,不正确的输入格式");
            }
            paraMaps.putAll((Map<String, Object>)parseObj);
        } catch (Exception e) {
            throw new RuntimeException("读取json内容失败", e);
        }
        Request req = new Request();
        req.setParaMaps(paraMaps);
        req.setContentType(parseContentType(request));
        req.setEncoding(parseEncoding(request));
        req.setRequestPattern(buildRequestPattern(request));
        return req;
    }
}

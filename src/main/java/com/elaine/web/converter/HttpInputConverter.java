package com.elaine.web.converter;

import com.elaine.web.constants.ContentType;
import com.elaine.web.model.Request;
import com.elaine.web.model.RequestPattern;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jianlan on 15-9-4.
 * http输入转换器，完成HttpServletRequest到Request的转换。
 */
public abstract class HttpInputConverter {
    public abstract Request convert(HttpServletRequest request);

    /**
     * 从contentType报头中提取contentType
     * @param req
     * @return
     */
    protected static ContentType parseContentType(HttpServletRequest req) {
        String contentTypeStr = req.getContentType();
        ContentType contentType = null;
        if (contentTypeStr != null) {
            contentType = ContentType.textOf(contentTypeStr.split(";")[0]);
        }
        return contentType;
    }

    /**
     * 解析编码字符串
     * @param req
     * @return
     */
    protected static String parseEncoding(HttpServletRequest req) {
        String encoding = null;
        String contentTypeStr = req.getContentType();
        if (contentTypeStr != null) {
            encoding = contentTypeStr.split("=")[1].trim();
        }
        return encoding;
    }

    /**
     * 构建requestPattern，用于路由
     * @param req
     * @return
     */
    protected static RequestPattern buildRequestPattern(HttpServletRequest req) {
        RequestPattern requestPattern = null;
        requestPattern.setHttpMethod(req.getMethod());
        requestPattern.setUrl(req.getRequestURI());
        return requestPattern;
    }
}

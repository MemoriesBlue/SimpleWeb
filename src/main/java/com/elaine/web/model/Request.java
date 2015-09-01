package com.elaine.web.model;

import com.elaine.web.constants.ContentType;

import java.util.Map;

/**
 * Created by jianlan on 15-8-29.
 */
public class Request {
    private Map<String, Object> paraMaps;
    private ContentType contentType;
    private String encoding;
    private RequestPattern requestPattern;

    public Map<String, Object> getParaMaps() {
        return paraMaps;
    }

    public void setParaMaps(Map<String, Object> paraMaps) {
        this.paraMaps = paraMaps;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public RequestPattern getRequestPattern() {
        return requestPattern;
    }

    public void setRequestPattern(RequestPattern requestPattern) {
        this.requestPattern = requestPattern;
    }
}

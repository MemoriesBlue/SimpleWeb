package com.elaine.web.constants;

import com.elaine.web.model.common.EnumTrait;

/**
 * Created by jianlan on 15-7-30.
 */
public enum HttpMethod implements EnumTrait {
    GET(1, "get"),
    POST(2, "post"),
    OTHER(3, "other")
    ;
    private String text;
    private int code;

    private HttpMethod(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static HttpMethod codeOf(int code) {
        HttpMethod[] httpMethods = HttpMethod.values();
        for (HttpMethod httpMethod : httpMethods) {
            if (httpMethod.code == code) {
                return httpMethod;
            }
        }
        return OTHER;
    }

    public static HttpMethod textOf(String text) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.text.equalsIgnoreCase(text)) {
                return httpMethod;
            }
        }
        return OTHER;
    }
}

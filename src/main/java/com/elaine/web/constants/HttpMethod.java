package com.elaine.web.constants;

import com.elaine.web.model.common.EnumTrait;

/**
 * Created by jianlan on 15-7-30.
 */
public enum HttpMethod implements EnumTrait {
    GET(1, "get"),
    POST(2, "post")
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
}

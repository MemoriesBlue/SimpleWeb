package com.elaine.web.constants;

import com.elaine.blog.model.common.EnumTrait;

/**
 * Created by jianlan on 15-8-1.
 * 一般GET方式提交的ContentType为null.
 * 普通表单post提交的ContentType为APPLICATION_FORM_URLENCODED
 * 带有file属性的表单post提交的ContentType为APPLICATION_FORM_MULTIPART
 */
public enum ContentType implements EnumTrait {
    NOT_KNOWN(0, "null"),
    APPLICATION_FORM_URLENCODED(1, "application/x-www-form-urlencoded"),
    APPLICATION_FORM_MULTIPART(2, "multipart/form-data"),
    APPLICATION_JSON(3, "application/json")
    ;
    private int code;
    private String text;

    private ContentType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public ContentType codeOf(int code) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.code == code) {
                return contentType;
            }
        }
        return NOT_KNOWN;
    }

    public ContentType textOf(String text) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.text.equals(text)) {
                return contentType;
            }
        }
        return NOT_KNOWN;
    }
}

package com.elaine.web.converter;

import com.elaine.web.constants.ContentType;
import com.elaine.web.constants.HttpMethod;

/**
 * Created by jianlan on 15-9-4.
 */
public class HttpInputConverterSelector {
    private static final HttpInputConverter urlEncodeConverter = new UrlEncodeConverter();
    private static final HttpInputConverter multipartFormConverter = new MultipartFormConverter();
    private static final HttpInputConverter httpJsonConverter = new HttpJsonConverter();

    public static HttpInputConverter select(HttpMethod method, ContentType contentType) {
        if (method == HttpMethod.GET) {
            return urlEncodeConverter;
        }
        if ((method == HttpMethod.POST) && (contentType == ContentType.APPLICATION_FORM_MULTIPART)) {
            return multipartFormConverter;
        }
        if ((method == HttpMethod.POST) && (contentType == ContentType.APPLICATION_JSON)) {
            return httpJsonConverter;
        }
        return null;
    }
}

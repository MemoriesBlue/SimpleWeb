package com.elaine.web.bin;

import com.elaine.web.constants.ContentType;
import com.elaine.web.constants.HttpMethod;
import com.elaine.web.converter.HttpInputConverter;
import com.elaine.web.converter.HttpInputConverterSelector;
import com.elaine.web.model.Request;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jianlan on 15-9-3.
 */
public class HttpInputResolver {
    public static Request solve(HttpServletRequest request) {
        ContentType contentType = null;
        HttpMethod httpMethod = HttpMethod.textOf(request.getMethod());
        String contentTypeStr = request.getContentType();
        if (contentTypeStr != null) {
            contentType = ContentType.textOf(contentTypeStr.split(";")[0].trim());
        }
        HttpInputConverter converter = HttpInputConverterSelector.select(httpMethod, contentType);
        return converter.convert(request);
    }
}

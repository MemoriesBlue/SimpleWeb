package com.elaine.web.api.annotations;

import com.elaine.web.constants.HttpMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jianlan on 15-7-29.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    public String value();
    public HttpMethod method() default HttpMethod.GET;

}

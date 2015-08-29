package com.elaine.web.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by jianlan on 15-7-30.
 */
public class Request {
    private String url;
    private String httpMethod;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Request)) {
            return false;
        }
        Request r = (Request) obj;
        return StringUtils.equals(this.url, r.url) && StringUtils.equalsIgnoreCase(this.httpMethod, r.httpMethod);
    }

    @Override
    public int hashCode() {
        return (this.httpMethod + this.url).hashCode();
    }

    @Override
    public String toString() {
        return "{" + httpMethod + " " + url + "}";
    }
}

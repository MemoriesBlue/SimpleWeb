package com.elaine.web.model;

import java.lang.reflect.Method;

/**
 * Created by jianlan on 15-7-30.
 */
public class Handler {
    private Object controller;
    private Method method;

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}

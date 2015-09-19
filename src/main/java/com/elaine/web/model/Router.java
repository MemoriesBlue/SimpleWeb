package com.elaine.web.model;

import com.elaine.web.api.annotations.Param;
import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by jianlan on 15-8-15.
 */
public class Router {
    private Map<RequestPattern, Handler> requestHandlerMap;

    public void setRequestHandlerMap(Map<RequestPattern, Handler> requestHandlerMap) {
        this.requestHandlerMap = requestHandlerMap;
    }

    public Handler route(RequestPattern requestPattern) {
        if (requestHandlerMap == null)
            throw new NullPointerException("没有构建路由信息，路由映射表为null");
        if (requestPattern == null)
            throw new NullPointerException("请求信息Request为null，无法路由");
        Handler handler = requestHandlerMap.get(requestPattern);
        if (handler == null)
            throw new NullPointerException("没有找到处理器信息，Request：" + requestPattern);
        return handler;
    }
}

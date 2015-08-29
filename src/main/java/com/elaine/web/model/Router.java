package com.elaine.web.model;

import java.util.Map;

/**
 * Created by jianlan on 15-8-15.
 */
public class Router {
    private Map<Request, Handler> requestHandlerMap;

    public void setRequestHandlerMap(Map<Request, Handler> requestHandlerMap) {
        this.requestHandlerMap = requestHandlerMap;
    }

    public Handler route(Request request) {
        if (requestHandlerMap == null)
            throw new NullPointerException("没有构建路由信息，路由映射表为null");
        if (request == null)
            throw new NullPointerException("请求信息Request为null，无法路由");
        Handler handler = requestHandlerMap.get(request);
        if (handler == null)
            throw new NullPointerException("没有找到处理器信息，Request：" + request);
        return handler;
    }
}

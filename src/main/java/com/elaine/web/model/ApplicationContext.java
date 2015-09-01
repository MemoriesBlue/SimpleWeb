package com.elaine.web.model;

import java.util.List;
import java.util.Map;

/**
 * Created by jianlan on 15-8-29.
 * 应用上下文，管理bean，以及维护路由信息。
 */
public class ApplicationContext {
    private List<Object> controllers;
    private Map<String, Object> beanMaps;
    private Router router;

    public void setControllers(List<Object> controllers) {
        if (controllers != null) {
            throw new UnsupportedOperationException("不支持重复设置控制器信息");
        }
        this.controllers = controllers;
    }

    public void setBeanMaps(Map<String, Object> beanMaps) {
        if (beanMaps != null) {
            throw new UnsupportedOperationException("不支持重复设置bean容器信息");
        }
        this.beanMaps = beanMaps;
    }

    public Router getRouter() {
        if (router == null) {
            throw new NullPointerException("router为null，路由信息没有正确生成。");
        }
        return router;
    }
}

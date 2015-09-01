package com.elaine.web.bin;

import com.elaine.web.api.annotations.Controller;
import com.elaine.web.api.annotations.Service;
import com.elaine.web.model.ApplicationContext;
import com.elaine.web.utils.ClassUtils;
import com.elaine.web.utils.ReflectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by jianlan on 15-8-29.
 */
public class ApplicationContextBuilder {
    public ApplicationContext build() {
        try {
            List<String> scanClasses = ClassUtils.scanClasses("");
            final List<Object> controllers = Lists.newArrayList();
            final Map<Class<?>, Object> beanMaps = Maps.newHashMap();
            ClassUtils.traverseClassList(scanClasses, new ClassUtils.Traverse() {
                public void traverse(String classNameStr) {
                    try {
                        Class<?> cls = Class.forName(classNameStr);
                        Controller controller = cls.getAnnotation(Controller.class);
                        Object obj = cls.newInstance();
                        if (controller != null) {
                            controllers.add(obj);
                        }
                        Service service = cls.getAnnotation(Service.class);
                        if (service != null) {
                            beanMaps.put(cls, obj);
                        }
                    } catch (Throwable t) {
                        throw new RuntimeException("无法构建bean", t);
                    }
                }
            });
            return null;
        } catch (Exception e) {
            throw new RuntimeException("构建ApplicationContext失败", e);
        }
    }

    /**
     * 完成bean之间的依赖注入
     * @param beanMaps beans
     */
    private void processBeans(Map<Class<?>, Object> beanMaps) {
        for (Map.Entry<Class<?>, Object> entry : beanMaps.entrySet()) {
            Class<?> cls = entry.getKey();
            Object bean = entry.getValue();
            ReflectionUtils.doWithFields(cls, new ReflectionUtils.FieldVisitor() {
                public void visit(Field field) {
                }
            });
        }
    }
}

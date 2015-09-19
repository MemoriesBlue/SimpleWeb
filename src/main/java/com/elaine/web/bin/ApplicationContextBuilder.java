package com.elaine.web.bin;

import com.elaine.web.api.annotations.Controller;
import com.elaine.web.api.annotations.Param;
import com.elaine.web.api.annotations.RequestMapping;
import com.elaine.web.api.annotations.Service;
import com.elaine.web.model.ApplicationContext;
import com.elaine.web.model.Handler;
import com.elaine.web.model.RequestPattern;
import com.elaine.web.model.Router;
import com.elaine.web.utils.ClassUtils;
import com.elaine.web.utils.ReflectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
            final Map<String, Object> beanMaps = Maps.newHashMap();
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
                            beanMaps.put(cls.getName(), obj);
                        }
                    } catch (Throwable t) {
                        throw new RuntimeException("无法构建bean", t);
                    }
                }
            });
            processBeans(beanMaps);
            Router router = buildRouter(controllers, beanMaps);
            ApplicationContext context = new ApplicationContext();
            context.setBeanMaps(beanMaps);
            context.setControllers(controllers);
            context.setRouter(router);
            return context;
        } catch (Exception e) {
            throw new RuntimeException("构建ApplicationContext失败", e);
        }
    }

    /**
     * 完成bean之间的依赖注入
     * @param beanMaps beans
     */
    private void processBeans(final Map<String, Object> beanMaps) throws ClassNotFoundException {
        for (Map.Entry<String, Object> entry : beanMaps.entrySet()) {
            String classNameStr = entry.getKey();
            final Object bean = entry.getValue();
            wireFields(bean, classNameStr, beanMaps);
        }
    }

    /**
     * 注入指定bean的属性
     * @param bean
     * @param classNameStr
     * @param beanMaps
     */
    private void wireFields(final Object bean, String classNameStr, final Map<String, Object> beanMaps) throws ClassNotFoundException {
        Class cls = Class.forName(classNameStr);
        ReflectionUtils.doWithFields(cls, new ReflectionUtils.FieldVisitor() {
            public void visit(Field field) {
                Resource resource = field.getAnnotation(Resource.class);
                if (resource != null) {
                    Object fieldBean = beanMaps.get(field.getClass().getName());
                    if (fieldBean == null) {
                        throw new RuntimeException("dependent bean not found: " + field.getClass().getName() + "-->" + field.getName());
                    }
                    field.setAccessible(true);
                    try {
                        field.set(bean, fieldBean);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("注入" + field.getClass().getName() + "-->" + field.getName() + "失败", e);
                    }
                }
            }
        });
    }

    /**
     * 构建路由器
     * @param controllers
     * @param beanMaps
     * @return
     */
    private Router buildRouter(List<Object> controllers, Map<String, Object> beanMaps) throws ClassNotFoundException {
        Router router = new Router();
        final Map<RequestPattern, Handler> requestHandlerMaps = Maps.newHashMap();
        for (final Object controller : controllers) {
            wireFields(controller, controller.getClass().getName(), beanMaps);
            Class cls = controller.getClass();
            final String baseUri = ((Controller)cls.getAnnotation(Controller.class)).urlMapping();
            ReflectionUtils.doWithMethods(cls, new ReflectionUtils.MethodVisitor() {
                public void visit(Method method) {
                    RequestMapping requestMapping = (RequestMapping)method.getAnnotation(RequestMapping.class);
                    if (requestMapping != null) {
                        RequestPattern requestPattern = new RequestPattern();
                        requestPattern.setUrl(baseUri + requestMapping.value());
                        requestPattern.setHttpMethod(requestMapping.method().getText());

                        Handler handler = new Handler();
                        handler.setController(controller);
                        handler.setMethod(method);

                        requestHandlerMaps.put(requestPattern, handler);
                    }
                }
            });
        }
        router.setRequestHandlerMap(requestHandlerMaps);
        return router;
    }
}

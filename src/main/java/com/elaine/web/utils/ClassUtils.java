package com.elaine.web.utils;

import com.elaine.blog.annotation.Controller;
import com.elaine.blog.annotation.RequestMapping;
import com.elaine.blog.annotation.Service;
import com.elaine.blog.model.Handler;
import com.elaine.blog.model.Request;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jianlan on 15-7-27.
 */
public class ClassUtils {

    public static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    private static final String CLASS_SUFFIX = ".class";
    public static final Class<? extends Annotation> CONTROLLER_CLASS = Controller.class;
    public static final Class<? extends Annotation> SERVICE_CLASS = Service.class;

    /**
     * package名称换成相对路径
     * @param packageName
     * @return
     */
    public static String package2Path(String packageName) {
        return packageName.replace(".", File.separator);
    }

    /**
     * 路径转换成package名称
     * @param path
     * @return
     */
    public static String path2Package(String path) {
        return path.replaceAll("[/\\\\]", ".");
    }

    /**
     * 扫描基本包下的所有类
     * @param basePackage 包名
     * @return
     * @throws Exception
     */
    public static List<String> scanClasses(String basePackage) throws Exception {
        final List<String> clsStrList = Lists.newLinkedList();
        final Path basePath = Paths.get(Thread.currentThread().getContextClassLoader().getResource("").toURI());
        Path clsPath = basePath.resolve(basePackage.replace(".", File.separator));
        Files.walkFileTree(clsPath, new FileVisitor<Path>() {
            private String basePathStr = basePath.toString();

            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(CLASS_SUFFIX)) {
                    String fileName = file.toString();
                    String clsName = fileName.substring(fileName.indexOf(basePathStr) + basePathStr.length() + 1, fileName.lastIndexOf(CLASS_SUFFIX)).replaceAll("[/\\\\]", ".");
                    clsStrList.add(clsName);
                }
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        return clsStrList;
    }
    public static List<Class<?>> AnnotationFilter(List<String> classStrList, Class<? extends Annotation> annotation) {
        List<Class<?>> clsList = Lists.newArrayList();
        for (String clsStr : classStrList) {
            Object obj = null;
            try {
                Class<?> cls = Class.forName(clsStr);
                Annotation target = cls.getAnnotation(annotation);
                if (target != null) {
                    clsList.add(cls);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clsList;
    }

    public static Map<Request, Handler> build(List<String> classStrList) {
        Map<Request, Handler> router = new HashMap<Request, Handler>();
        Map<Class<?>, Object> controllerMap = new HashMap<Class<?>, Object>();
        Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();
        for (String clsStr : classStrList) {
            try {
                Class<?> cls = Class.forName(clsStr);
                Annotation controller = cls.getAnnotation(CONTROLLER_CLASS);
                Object o = cls.newInstance();
                if (controller != null) {
                    controllerMap.put(cls, o);
                }
                Annotation service = cls.getAnnotation(SERVICE_CLASS);
                if (service != null) {
                    beanMap.put(cls, o);
                }
            } catch (Throwable t) {
                continue;
            }
        }
        Set<Class<?>> beanClassSet = beanMap.keySet();
        for (Class<?> beanClass : beanClassSet) {
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                Annotation resource = field.getAnnotation(Resource.class);
                if (resource != null) {
                    Class<?> fieldClassType = field.getType();
                    Object bean = beanMap.get(beanClass);
                    Object fieldResource = beanMap.get(fieldClassType);
                    if (fieldResource == null) {
                        logger.error("can't create bean: {}, field:{} not found!", beanClass.getName(), field.getName());
                        throw new RuntimeException("create bean :" + beanClass.getName() + " fail...");
                    }
                    field.setAccessible(true);
                    try {
                        field.set(bean, fieldResource);
                    } catch (IllegalAccessException e) {
                        logger.error("can't create bean: {}, field:{} setting fail!", beanClass.getName(), field.getName());
                        throw new RuntimeException("create bean :" + beanClass.getName() + " fail...");
                    }
                }
            }
        }
        Set<Class<?>> controllerClassSet = controllerMap.keySet();
        for (Class<?> controllerClass : controllerClassSet) {
            Field[] fields = controllerClass.getDeclaredFields();
            for (Field field : fields) {
                Annotation resource = field.getAnnotation(Resource.class);
                if (resource != null) {
                    Class<?> fieldClassType = field.getType();
                    Object bean = controllerMap.get(controllerClass);
                    Object fieldResource = beanMap.get(fieldClassType);
                    if (fieldResource == null) {
                        logger.error("can't create bean: {}, field:{} not found!", controllerClass.getName(), field.getName());
                        throw new RuntimeException("create bean :" + controllerClass.getName() + " fail...");
                    }
                    field.setAccessible(true);
                    try {
                        field.set(bean, fieldResource);
                    } catch (IllegalAccessException e) {
                        logger.error("can't create bean: {}, field:{} setting fail!", controllerClass.getName(), field.getName());
                        throw new RuntimeException("create bean :" + controllerClass.getName() + " fail...");
                    }
                }
            }
            Method[] methods = controllerClass.getDeclaredMethods();
            Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);
            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    String url = controllerAnnotation.urlMapping() + requestMapping.value();
                    String httpMethod = requestMapping.method().getText();
                    Request request = new Request();
                    request.setUrl(url);
                    request.setHttpMethod(httpMethod);

                    Handler handler = new Handler();
                    handler.setController(controllerMap.get(controllerClass));
                    handler.setMethod(method);

                    router.put(request, handler);
                }
            }
        }
        return router;
    }

    public static void handle(Map<Request, Handler> router, Request request) throws Exception{
        Handler handler = router.get(request);
        if (handler == null) {
            logger.error("no controller handle the request {}", request.getUrl());
            throw new RuntimeException("no controller handle the request:" + request.getUrl());
        }
        Method method = handler.getMethod();
        Object controller = handler.getController();
        method.invoke(controller);
    }
    
}

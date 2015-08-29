package com.elaine.web.model;

import com.elaine.web.api.annotations.Param;
import com.elaine.web.constants.ContentType;
import com.elaine.web.utils.JsonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Object;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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

    public Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestMethod = request.getMethod();
        if (requestMethod == null || "".equals(requestMethod.trim())) {
            throw new RuntimeException("can't parse the http request method,request:" + request.getRequestURI());
        }
        Object[] paras = parseParameter(request, requestMethod);
        return method.invoke(controller, paras);

    }
    private Object[] parseParameter(HttpServletRequest request, String requestMethod) throws Exception {
        String contentTypeStr = request.getContentType();
        int index = contentTypeStr.indexOf(";");
        if (index > 0) {
            contentTypeStr = contentTypeStr.substring(0, index);
        }
        Map parameterMap1 = request.getParameterMap();
        ContentType contentType = ContentType.textOf(contentTypeStr);
        Map<String, Object> parameterMap = Maps.newHashMap();
        List<Object> paraList = Lists.newArrayList();
        //对于get方式，直接获取parameter即可
        if ("get".equalsIgnoreCase(requestMethod)) {
            parameterMap.putAll(request.getParameterMap());
        }
        //对于post方式，还需要进一步区分json，form表单提交和带file的form表单提交
        if ("post".equalsIgnoreCase(requestMethod) && contentType == ContentType.APPLICATION_FORM_URLENCODED) {
            parameterMap.putAll(request.getParameterMap());
        }
        if ("post".equalsIgnoreCase(requestMethod) && contentType == ContentType.APPLICATION_FORM_MULTIPART) {
            ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
            List<FileItem> fileItems = servletFileUpload.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                parameterMap.put(fileItem.getFieldName(), fileItem);
            }
        }
        if ("post".equalsIgnoreCase(requestMethod) && contentType == ContentType.APPLICATION_JSON) {
            String jsonStr = IOUtils.toString(request.getInputStream());
            Object jsonObj = JsonUtils.parse(jsonStr);
            if ( !(jsonObj instanceof Map)) {
                throw new RuntimeException("json参数不是一个对象,而是" + jsonObj.getClass().getSimpleName());
            }

            parameterMap.putAll((Map<String, Object>)jsonObj);
        }
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; ++i) {
            Annotation[] annotations = parameterAnnotations[i];
            String paraName = null;
            for (Annotation annotation : annotations) {
                if (annotation instanceof Param) {
                    paraName = ((Param) annotation).value();
                    break;
                }
            }
            Class<?> paraType = parameterTypes[i];
            if (paraName == null || "".equals(paraName)) {
                //没有paramName
                if (paraType.equals(String.class) || paraType.isPrimitive()
                        || ((Class)paraType.getField("TYPE").get(null)).isPrimitive()) {
                    throw new RuntimeException("controller方法参数没有指定param值");
                }
                Object obj = paraType.newInstance();
                Field[] fields = paraType.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (parameterMap.get(fieldName) != null) {
                        Object fieldValue = parameterMap.get(fieldName);

                        field.setAccessible(true);
                        field.set(obj, parameterMap.get(fieldName));
                    }
                }
                paraList.add(obj);
                continue;
            } else {
                if (parameterMap.get(paraName) == null) {
                    throw new RuntimeException("无法解析controller方法参数，param=" + paraName);
                }
                Object para = parameterMap.get(paraName);
                Object tmp = para;
                if (tmp.getClass().isArray()) {
                    Object[] tempArr = (Object[])tmp;
                    para = tempArr[0];
                }

                paraList.add(paraType.cast(para));
                continue;
            }
        }
        return paraList.toArray();
    }


    private Object convert(Object originPara, Class<?> paraType) {
        if (originPara.getClass().isArray()) {
            //String[]
            String[] fieldValueArr = (String[]) originPara;
            if (!paraType.isArray()) {
                throw new RuntimeException("can't convert string[] to " + paraType.getName());
            }
            if (paraType == int[].class || paraType == Integer[].class) {

            }
        }
        return null;
    }

}

package com.elaine.web.utils;

import com.elaine.web.api.annotations.Param;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by jianlan on 15-9-13.
 */
public class ArgumentUtils {
    public Object[] getArguments(Method method, Map<String, Object> paraMaps) {
        List<Object> arguments = Lists.newArrayList();
        Class<?>[] paraTypes = method.getParameterTypes();
        Annotation[][] paraAnnotationsArr = method.getParameterAnnotations();
        for (int i = 0; i < paraTypes.length; ++i) {
            Class paraType = paraTypes[i];
            Annotation[] annotations = paraAnnotationsArr[i];
            String paraName = getParaName(annotations);
            if (StringUtils.isBlank(paraName) && isPrimitive(paraType)) {
                throw new RuntimeException("参数转换失败,请使用@Param注解，并且指定参数key，method:" + method.getName());
            }

            //自定义JavaBean
            if(!isPrimitive(paraType)) {

            }
        }
        return arguments.toArray();
    }

    /**
     * 通过Param注解来获取参数key
     * @param annotations
     * @return
     */
    private static String getParaName(Annotation[] annotations) {
        String paraName = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Param) {
                paraName = ((Param)annotation).value();
                break;
            }
        }
        return paraName;
    }

    /**
     * 判断指定类型是否不是自定义JavaBean(剔除基础类型，数组类型和集合类型，Map类型)
     * @param type
     * @return
     */
    public static boolean isPrimitive(Class type) {
        if (type.isPrimitive()
                || type.isArray()
                || Collection.class.isAssignableFrom(type)
                || Map.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    private void fillBean(final Object bean, final Map<String, Object> paraMaps) {
        Class<?> beanClass = bean.getClass();
        ReflectionUtils.doWithFields(beanClass, new ReflectionUtils.FieldVisitor() {
            public void visit(Field field) {
                Class<?> fieldType = field.getType();
                if (isPrimitive(fieldType)) {
                    //原型类型，不需要继续填充
                } else {
                    //JavaBean，继续填充
                    Object fieldObj = null;
                    try {
                        fieldObj = fieldType.newInstance();
                        fillBean(fieldObj, paraMaps);
                        field.set(bean, fieldObj);
                    } catch (Exception e) {
                        throw new RuntimeException("生成属性:" + field.getName() + "失败");
                    }
                }
            }
        });
    }

}

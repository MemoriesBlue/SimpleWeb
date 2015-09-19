package com.elaine.web.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by jianlan on 15-8-21.
 * 用于方便反射
 */
public class ReflectionUtils {

    /**
     * 用于Field遍历操作
     */
    public interface FieldVisitor {
        void visit(Field field);
    }

    /**
     * 用于Field遍历操作
     */
    public interface MethodVisitor {
        void visit(Method method);
    }

    /**
     * 按照给出的FieldVisitor来遍历cls的所有Field
     * @param cls
     * @param visitor
     */
    public static void doWithFields(Class<?> cls, FieldVisitor visitor) {
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
                visitor.visit(field);
                field.setAccessible(false);
                continue;
            }
            visitor.visit(field);
        }
    }

    public static void doWithMethods(Class<?> cls, MethodVisitor visitor) {
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            visitor.visit(method);
        }
    }
}

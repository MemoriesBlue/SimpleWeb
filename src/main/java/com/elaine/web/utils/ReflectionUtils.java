package com.elaine.web.utils;

import java.lang.reflect.Field;

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
}

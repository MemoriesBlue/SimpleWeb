package com.elaine.web.type;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by jianlan on 15-9-19.
 *
 */
public class TypeUtils {
    private static final Map<Class, Integer> typePriorityMap = Maps.newHashMap();

    static {
        typePriorityMap.put(byte.class, 1);
        typePriorityMap.put(short.class, 1);
        typePriorityMap.put(int.class, 1);
        typePriorityMap.put(long.class, 1);
        typePriorityMap.put(float.class, 1);
        typePriorityMap.put(double.class, 1);
        typePriorityMap.put(boolean.class, 1);

        typePriorityMap.put(Byte.class, 1);
        typePriorityMap.put(Short.class, 1);
        typePriorityMap.put(Integer.class, 1);
        typePriorityMap.put(Long.class, 1);
        typePriorityMap.put(Float.class, 1);
        typePriorityMap.put(Double.class, 1);
        typePriorityMap.put(Boolean.class, 1);

        typePriorityMap.put(byte[].class, 1);
        typePriorityMap.put(short[].class, 1);
        typePriorityMap.put(int[].class, 1);
        typePriorityMap.put(long[].class, 1);
        typePriorityMap.put(float[].class, 1);
        typePriorityMap.put(double[].class, 1);
        typePriorityMap.put(boolean[].class, 1);

        typePriorityMap.put(Byte[].class, 1);
        typePriorityMap.put(Short[].class, 1);
        typePriorityMap.put(Integer[].class, 1);
        typePriorityMap.put(Long[].class, 1);
        typePriorityMap.put(Float[].class, 1);
        typePriorityMap.put(Double[].class, 1);
        typePriorityMap.put(Boolean.class, 1);

        typePriorityMap.put(String.class, 1);
        typePriorityMap.put(short.class, 1);
        typePriorityMap.put(short.class, 1);
        typePriorityMap.put(short.class, 1);
        typePriorityMap.put(short.class, 1);
        typePriorityMap.put(short.class, 1);
        typePriorityMap.put(short.class, 1);
        typePriorityMap.put(short.class, 1);
        typePriorityMap.put(short.class, 1);
    }
}

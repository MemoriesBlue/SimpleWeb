package com.elaine.web.type;

/**
 * Created by jianlan on 15-9-20.
 */
public interface TypeConverter {
    <S, T> T convert(S sourceObj);
}

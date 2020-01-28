package io.github.lmikoto.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import java.util.List;

@SuppressWarnings("all")
public abstract class JacksonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper mapper() {
        return objectMapper;
    }

    public static final String toJson(Object o) {
        if(o==null){
            return null;
        }
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception ex) {
            throw new RuntimeException("failed to serialize to json for : " + o, ex);
        }
    }

    public static final <T> T fromJson(String json, Class<T> clazz) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (Exception ex) {
                throw new RuntimeException("failed to serialize [" + json + "] to " + clazz, ex);
            }
        }
    }

    public static final <T> T fromJson(String json, JavaType javaType) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, javaType);
            } catch (Exception ex) {
                throw new RuntimeException("failed to serialize [" + json + "] to " + javaType, ex);
            }
        }
    }

    public static final <T> T fromJson(String json,  TypeReference<T> typeReference) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, typeReference);
            } catch (Exception ex) {
                throw new RuntimeException("failed to serialize [" + json + "] to " + typeReference, ex);
            }
        }
    }

    public static final <T> T convert(Object src,Class<T> dest){
        return objectMapper.convertValue(src, dest);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> json2List(String json, Class<T> clazz){
        if (Strings.isNullOrEmpty(json)) {
            return null;
        } else {
            try {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class,
                        clazz);
                return (List<T>)objectMapper.readValue(json,
                        javaType);
            } catch (Exception ex) {
                throw new RuntimeException("failed to serialize [" + json + "] to " + clazz + " list", ex);
            }
        }
    }
}
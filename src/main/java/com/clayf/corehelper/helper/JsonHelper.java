package com.clayf.corehelper.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * json 工具类
 * created by f at 2021-12-09 22:44
 */
public final class JsonHelper {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper()
                .registerModules(new JavaTimeModule());
    }

    private JsonHelper() {
    }

    /**
     * 将 t 转化为json
     *
     * @param t   任意对象
     * @param <T> 泛型
     * @return json
     */
    public static <T> String fromObjToJson(T t) {
        try {
            return OBJECT_MAPPER.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json，转化为指定clazz
     *
     * @param json json String
     * @param clazz 目标类
     * @param <T> 泛型
     * @return 要被转化的类型
     */
    public static <T> T fromJsonToObj(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

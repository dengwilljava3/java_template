package com.ay.common.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class JSONUtil {

    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
//        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
//        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }

    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            // SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null，输出为""，而不是null
            SerializerFeature.WriteNonStringKeyAsString};

    public static <T> T json2Bean(String json, Class<T> t) {
        if (StringUtil.isNull(json)) {
            return null;
        }
        return JSON.parseObject(json, t);
    }

    public static <T> String bean2Json(T t) {
        if (t == null) {
            return null;
        }
        return JSON.toJSONString(t);
    }

    /**
     * 显示需要的属性
     */
    public static <T> String bean2Json(T t, String... properties) {
        if (t == null) {
            return null;
        }
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(properties);
        return JSON.toJSONString(t, filter);
    }

    /**
     * 过滤null值
     * 
     * @param json
     * @return
     */
    public static Map<String, Object> json2Map(String json) {
        return JSONObject.parseObject(json, new TypeReference<Map<String, Object>>() {});
    }

    public static String map2Json(Map<?, ?> map) {
        return map2Json(map, true);
    }

    public static String map2Json(Map<?, ?> map, boolean filterNull) {
        if (filterNull) {
            return JSON.toJSONString(map, features);
        }
        return JSON.toJSONString(map);
    }

    public static <T> T json2CustomerBean(String json) {
        return JSONObject.parseObject(json, new TypeReference<T>() {});
    }

    public static SerializerFeature[] getFeatures() {
        return features;
    }


}

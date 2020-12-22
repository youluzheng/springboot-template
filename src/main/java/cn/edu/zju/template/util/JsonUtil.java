package cn.edu.zju.template.util;


import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.edu.zju.template.exception.InnerException;
import cn.edu.zju.template.exception.KeyNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.TimeZone;


public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // write多余字段忽略
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略getter，setter
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        // 空字段忽略
//		mapper.setSerializationInclusion(Include.NON_EMPTY);
        // 设置时区
        mapper.setTimeZone(TimeZone.getDefault());
    }

    public static <T> T str2Object(String jsonStr, Class<T> clazz) {
        try {
            return JsonUtil.mapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            log.debug("jsonStr:{}, clazz:{}", jsonStr, clazz.getName());
            log.error(e.getMessage());
            throw new InnerException();
        }
    }

    public static String Object2Str(Object obj) {
        try {
            return JsonUtil.mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.debug("obj:{}", obj.getClass().getName());
            log.error(e.getMessage());
            throw new InnerException();
        }
    }

    /**
     * 从jsonStr中获取某个键的值
     *
     * @param jsonStr
     * @param key
     * @return
     * @throws KeyNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static Object getItem(String jsonStr, String key) throws KeyNotFoundException {
        HashMap<String, Object> tempMap = JsonUtil.str2Object(jsonStr, HashMap.class);
        if (!tempMap.containsKey(key)) {
            log.error("{}未找到", key);
            throw new KeyNotFoundException(412, key + "未找到");
        }
        return tempMap.get(key);
    }

    /**
     * 从jsonStr中获取某个键的值，返回optional对象
     *
     * @param jsonStr
     * @param key
     * @return
     */
    public static Optional<Object> getItemOptional(String jsonStr, String key) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> tempMap = JsonUtil.str2Object(jsonStr, HashMap.class);
        return Optional.ofNullable(tempMap.getOrDefault(key, null));
    }

}

package cn.edu.zju.template.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.zju.template.exception.KeyNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class RequestUtil {
    private static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);
    private Map<String, Object> map = new HashMap<String, Object>();

    public RequestUtil(Map<String, Object> map) {
        this.map = map;
    }

    @SuppressWarnings("unchecked")
    public RequestUtil(String json) {
        this.map = JsonUtil.str2Object(json, Map.class);
    }

    public <T> T getObject(Class<T> clazz) {
        T t = null;
        String str = JsonUtil.Object2Str(this.map);
        t = JsonUtil.str2Object(str, clazz);
        return t;
    }

    public Object getItem(String key) throws KeyNotFoundException {
        if (!this.map.containsKey(key)) {
            log.error("{}未找到", key);
            throw new KeyNotFoundException(412, key + "未找到");
        }
        return map.get(key);
    }

    public Optional<Object> getItemOptional(String key) {
        return Optional.ofNullable(map.getOrDefault(key, null));
    }

}

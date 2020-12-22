package cn.edu.zju.template.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 32190
 * {@code code} 200 表示正常
 */
public class ResponseUtil {
    private static final ResponseUtil defaultResponse = new ResponseUtil();
    private int code = 200;
    private String msg = "success";
    private Map<String, Object> data = new HashMap<String, Object>();

    public ResponseUtil() {
    }

    public ResponseUtil(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseUtil(String key, Object value) {
        this.data.put(key, value);
    }

    public ResponseUtil(Map<String, Object> map) {
        this.data.putAll(map);
    }

    public static ResponseUtil getDefaultResponse() {
        return ResponseUtil.defaultResponse;
    }

    public ResponseUtil add(Map<String, Object> map) {
        this.data.putAll(map);
        return this;
    }

    public ResponseUtil add(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public void error(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String toJsonStr() {
        String res = JsonUtil.Object2Str(this);
        return res;
    }

    @Override
    public String toString() {
        return this.toJsonStr();
    }
}

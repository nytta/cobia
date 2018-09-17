package lam.cobia.core.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: BaseParameter
 * @author: linanmiao
 * @date: 2018/9/17 17:49
 * @version: 1.0
 */
public class BaseParameter implements Parameterable{

    private Map<String, Object> params;

    public BaseParameter(Map<String, Object> params) {
        this.params = params == null ? new HashMap<String, Object>() : params;
    }

    @Override
    public void setParam(String key, Object value) {
        this.params.put(key, String.valueOf(value));
    }

    @Override
    public String getParam(String key) {
        return String.valueOf(this.params.get(key));
    }

    @Override
    public String getParam(String key, String defaultValue) {
        Object value;
        return (value = this.params.get(key)) != null ? String.valueOf(value) : defaultValue;
    }

    @Override
    public int getParamInt(String key) {
        Object value = this.params.get(key);
        return Integer.parseInt(value.toString());
    }

    @Override
    public int getParamInt(String key, int defaultValue) {
        Object value = this.params.get(key);
        return value == null ? defaultValue : Integer.parseInt(value.toString());
    }

    @Override
    public boolean getParamBoolean(String key) {
        Object value = this.params.get(key);
        return Boolean.valueOf(value.toString());
    }

    @Override
    public boolean getParamBoolean(String key, boolean defaultValue) {
        Object value = this.params.get(key);
        return value == null ? defaultValue : Boolean.valueOf(value.toString());
    }

    @Override
    public void close() throws IOException {
        this.params.clear();
    }
}

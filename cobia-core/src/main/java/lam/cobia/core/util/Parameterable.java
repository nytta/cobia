package lam.cobia.core.util;

import java.io.Closeable;

/**
 * @description: Parameterable
 * @author: linanmiao
 * @date: 2018/7/22 23:24
 * @version: 1.0
 */
public interface Parameterable extends Closeable {

    public void setParam(String key, Object value);

    public String getParam(String key);

    public String getParam(String key, String defaultValue);

    public int getParamInt(String key);

    public int getParamInt(String key, int defaultValue);

    public boolean getParamBoolean(String key);

    public boolean getParamBoolean(String key, boolean defaultValue);

}

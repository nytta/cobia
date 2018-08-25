package lam.cobia.core.util;

import java.util.Map;

/**
 * @description: ParameterMap
 * @author: linanmiao
 * @date: 2018/8/25 17:53
 * @version: 1.0
 */
public interface ParameterMap {

    Map<String, Object> getParamMap();

    void setParamMap(Map<String, Object> param);
}

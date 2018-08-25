package lam.cobia.registry;

import java.util.Map;

/**
 * @description: AbstractRegistryProvider
 * @author: linanmiao
 * @date: 2018/8/25 17:21
 * @version: 1.0
 */
public abstract class AbstractRegistryProvider implements RegistryProvider{

    private Map<String, Object> paramMap;

    //protected RegistryProvider registryProvider;

    public AbstractRegistryProvider() {
        //this.registryProvider = registryProvider;
    }

    @Override
    public Map<String, Object> getParamMap() {
        return this.paramMap;
    }

    @Override
    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}

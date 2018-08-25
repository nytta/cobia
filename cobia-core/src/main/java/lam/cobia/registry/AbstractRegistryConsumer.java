package lam.cobia.registry;

import java.util.Map;

/**
 * @description: AbstractRegistry
 * @author: linanmiao
 * @date: 2018/8/25 17:17
 * @version: 1.0
 */
public abstract class AbstractRegistryConsumer implements RegistryConsumer{

    private Map<String, Object> paramMap;

    //protected RegistryConsumer registryConsumer;

    public AbstractRegistryConsumer() {
        //this.registryConsumer = registryConsumer;
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

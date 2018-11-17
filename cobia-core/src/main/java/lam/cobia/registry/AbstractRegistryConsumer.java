package lam.cobia.registry;

import lam.cobia.core.exception.CobiaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @description: AbstractRegistry
 * @author: linanmiao
 * @date: 2018/8/25 17:17
 * @version: 1.0
 */
public abstract class AbstractRegistryConsumer implements RegistryConsumer{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRegistryProvider.class);

    private Map<String, Object> paramMap;

    public AbstractRegistryConsumer() {
    }

    @Override
    public Map<String, Object> getParamMap() {
        if (this.paramMap == null) {
            throw new CobiaException("paramMap is null, please set paramMap at first.");
        }
        return this.paramMap;
    }

    @Override
    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}

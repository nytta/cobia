package lam.cobia.registry.impl;

import java.util.List;

import lam.cobia.core.model.RegistryData;
import lam.cobia.log.Logger;
import lam.cobia.log.LoggerFactory;
import lam.cobia.registry.AbstractRegistryConsumer;
import lam.cobia.registry.RegistrySubcriber;

/**
 * @author nytta
 * @date 2019/3/22
 */
public class NacosRegistryConsumer extends AbstractRegistryConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NacosRegistryConsumer.class);

    @Override
    public <T> List<RegistryData> getProviders(final Class<T> interfaceClass, final RegistrySubcriber registrySubcriber) {
        // TODO
        return null;
    }
}

package lam.cobia.registry.impl;

import java.util.Map;

import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.model.RegistryData;
import lam.cobia.log.Logger;
import lam.cobia.log.LoggerFactory;
import lam.cobia.registry.AbstractRegistryProvider;
import lam.cobia.rpc.support.Provider;

/**
 * @author nytta
 * @date 2019/3/22
 */
public class NacosRegistryProvider extends AbstractRegistryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(NacosRegistryProvider.class);

    @Override
    public <T> boolean registry(final Provider<T> provider, final HostAndPort hap, final Map<String, Object> params) {
        // TODO
        return false;
    }

    @Override
    public <T> void onProviderDataChanges(final Provider<T> provider, final RegistryData registryData) {
        // TODO
    }

    @Override
    public <T> RegistryData readRegistryData(final Provider<T> provider) {
        // TODO
        return null;
    }
}

package lam.cobia.registry.impl;

import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.registry.AbstractRegistryProvider;
import lam.cobia.rpc.support.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @description: DirectRegistryProvider
 * @author: linanmiao
 * @date: 2018/7/26 0:20
 * @version: 1.0
 */
public class DirectRegistryProvider extends AbstractRegistryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectRegistryProvider.class);

    public DirectRegistryProvider() {
        super();
    }

    @Override
    public <T> boolean registry(Provider<T> provider, HostAndPort hap, Map<String, Object> params) {
        LOGGER.info("[registry] provider[key:{}, interface:{}], registry host and port:[{}:{}]",
                provider.getKey(), provider.getInterface().getName(), hap.getHost(), hap.getPort());
        return true;
    }

    @Override
    public <T> void onProviderDataChanges(Provider<T> provider, RegistryData registryData) {
        LOGGER.debug("[onProviderDataChanges] provider:{}, registryData:{}", provider, GsonUtil.toJson(registryData));
    }

    @Override
    public <T> RegistryData readRegistryData(Provider<T> provider) {
        LOGGER.debug("[readRegistryData] provider:{}, return null.", provider);
        return null;
    }
}

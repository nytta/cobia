package lam.cobia.registry.impl;

import lam.cobia.config.spring.CRegistryBean;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.registry.AbstractRegistryProvider;
import lam.cobia.registry.RegistryProvider;
import lam.cobia.rpc.Provider;
import lam.cobia.spi.ServiceFactory;
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
}

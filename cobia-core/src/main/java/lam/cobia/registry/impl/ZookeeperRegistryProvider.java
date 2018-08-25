package lam.cobia.registry.impl;

import lam.cobia.config.spring.CRegistryBean;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.registry.AbstractRegistryProvider;
import lam.cobia.registry.RegistryProvider;
import lam.cobia.rpc.Provider;
import lam.cobia.spi.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: ZookeeperRegistryProvider
 * @author: linanmiao
 * @date: 2018/7/25 9:25
 * @version: 1.0
 */
public class ZookeeperRegistryProvider extends AbstractRegistryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistryProvider.class);

    public ZookeeperRegistryProvider() {
        super();
    }

    @Override
    public <T> boolean registry(Provider<T> provider, HostAndPort hap) {
        return false;
    }
}

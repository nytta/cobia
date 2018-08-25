package lam.cobia.registry.impl;

import java.util.List;

import lam.cobia.config.spring.CRegistryBean;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.registry.AbstractRegistryConsumer;
import lam.cobia.registry.RegistryConsumer;
import lam.cobia.spi.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: ZookeeperRegistryConsumer
 * @author: linanmiao
 * @date: 2018/7/25 9:42
 * @version: 1.0
 */
public class ZookeeperRegistryConsumer extends AbstractRegistryConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistryConsumer.class);

    public ZookeeperRegistryConsumer() {
        super();
    }

    @Override
    public <T> List<HostAndPort> getProviders(Class<T> interfaceClass) {
        return null;
    }
}

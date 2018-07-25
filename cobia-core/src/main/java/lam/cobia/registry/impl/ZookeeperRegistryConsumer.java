package lam.cobia.registry.impl;

import java.util.List;

import lam.cobia.core.model.HostAndPort;
import lam.cobia.registry.RegistryConsumer;

/**
 * @description: ZookeeperRegistryConsumer
 * @author: linanmiao
 * @date: 2018/7/25 9:42
 * @version: 1.0
 */
public class ZookeeperRegistryConsumer implements RegistryConsumer {

    @Override
    public <T> List<HostAndPort> getProviders(Class<T> interfaceClass) {
        return null;
    }
}

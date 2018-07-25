package lam.cobia.registry.impl;

import lam.cobia.core.model.HostAndPort;
import lam.cobia.registry.RegistryConsumer;

import java.util.List;

/**
 * @description: DirectRegistryConsumer
 * @author: linanmiao
 * @date: 2018/7/26 0:20
 * @version: 1.0
 */
public class DirectRegistryConsumer implements RegistryConsumer {
    @Override
    public <T> List<HostAndPort> getProviders(Class<T> interfaceClass) {
        return null;
    }
}

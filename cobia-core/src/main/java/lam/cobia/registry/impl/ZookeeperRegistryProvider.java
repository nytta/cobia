package lam.cobia.registry.impl;

import lam.cobia.core.model.HostAndPort;
import lam.cobia.registry.RegistryProvider;
import lam.cobia.rpc.Provider;

/**
 * @description: ZookeeperRegistryProvider
 * @author: linanmiao
 * @date: 2018/7/25 9:25
 * @version: 1.0
 */
public class ZookeeperRegistryProvider implements RegistryProvider {

    @Override
    public <T> boolean registry(Provider<T> provider, HostAndPort hap) {
        return false;
    }
}

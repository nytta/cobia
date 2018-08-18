package lam.cobia.registry;

import lam.cobia.core.model.HostAndPort;
import lam.cobia.rpc.Consumer;
import lam.cobia.spi.Spiable;

import java.util.List;

/**
 * @description: RegistryConsumer
 * @author: linanmiao
 * @date: 2018/7/24 23:37
 * @version: 1.0
 */
@Spiable("direct")
public interface RegistryConsumer {

    public <T> List<HostAndPort> getProviders(Class<T> interfaceClass);

}

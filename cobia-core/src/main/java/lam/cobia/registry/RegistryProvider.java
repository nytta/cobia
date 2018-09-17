package lam.cobia.registry;

import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.util.ParameterMap;
import lam.cobia.rpc.Provider;
import lam.cobia.spi.Spiable;

import java.util.Map;

/**
 * @description: RegistryProvider
 * @author: linanmiao
 * @date: 2018/7/24 23:37
 * @version: 1.0
 */
@Spiable("direct")
public interface RegistryProvider extends ParameterMap {

    public <T> boolean registry(Provider<T> provider, HostAndPort hap, Map<String, Object> params);

}

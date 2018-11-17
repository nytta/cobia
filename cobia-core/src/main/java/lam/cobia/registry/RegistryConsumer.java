package lam.cobia.registry;

import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.ParameterMap;
import lam.cobia.spi.Spiable;

import java.util.List;

/**
 * @description: RegistryConsumer
 * @author: linanmiao
 * @date: 2018/7/24 23:37
 * @version: 1.0
 */
@Spiable("direct")
public interface RegistryConsumer extends ParameterMap {

    public <T> List<RegistryData> getProviders(Class<T> interfaceClass, RegistrySubcriber registrySubcriber);

}

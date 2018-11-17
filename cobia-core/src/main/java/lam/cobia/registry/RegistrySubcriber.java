package lam.cobia.registry;

import java.util.List;

import lam.cobia.core.model.RegistryData;

/**
 * @author: linanmiao
 * @since 2018-10-08 22:11
 */
public interface RegistrySubcriber {

    public <T> void onProvidersChanges(Class<T> clazz, List<RegistryData> registryDatas);

    public <T> void onProviderChanges(Class<T> clazz, RegistryData registryData);

}

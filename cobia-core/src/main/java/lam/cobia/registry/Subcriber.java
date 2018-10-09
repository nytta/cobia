package lam.cobia.registry;

import java.util.List;

import lam.cobia.core.model.RegistryData;

/**
 * @author: linanmiao
 * @since 2018-10-08 22:11
 */
public interface Subcriber {

    public void subscribe(String interfaceName, List<RegistryData> registryDatas);

}

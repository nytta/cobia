package lam.cobia.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import lam.cobia.cluster.AbstractCluster;
import lam.cobia.core.model.RegistryData;

/**
 * @author: linanmiao
 */
public class ZookeeperSubcriber implements Subcriber{

    private static Logger LOGGER = LoggerFactory.getLogger(ZookeeperSubcriber.class);

    private AbstractCluster<?> abstractCluster;

    @Override
    public void subscribe(String interfaceName, List<RegistryData> registryDatas) {
        LOGGER.info("[subscribe] interface:{}, registryDatas:{}", interfaceName, registryDatas);
        abstractCluster.reloadConsumers(interfaceName, registryDatas);
    }

    public void setAbstractCluster(AbstractCluster<?> abstractCluster) {
        this.abstractCluster = abstractCluster;
    }
}

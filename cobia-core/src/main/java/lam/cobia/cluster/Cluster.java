package lam.cobia.cluster;

import lam.cobia.rpc.support.Consumer;
import lam.cobia.spi.Spiable;

/**
 * @description: Cluster
 * @author: linanmiao
 * @date: 2018/7/21 12:02
 * @version: 1.0
 */
@Spiable("failover")
public interface Cluster<T> extends Consumer<T> {
}

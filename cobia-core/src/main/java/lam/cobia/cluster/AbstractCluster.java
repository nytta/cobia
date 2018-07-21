package lam.cobia.cluster;

import lam.cobia.rpc.Invocation;
import lam.cobia.rpc.Result;

/**
 * @description: AbstractCluster
 * @author: linanmiao
 * @date: 2018/7/21 12:04
 * @version: 1.0
 */
public abstract class AbstractCluster<T> implements Cluster<T>{
    @Override
    public String getKey() {
        return null;
    }

    @Override
    public Class<T> getInterface() {
        return null;
    }

    @Override
    public Result invoke(Invocation invocation) {
        return null;
    }

    @Override
    public void close() {

    }
}

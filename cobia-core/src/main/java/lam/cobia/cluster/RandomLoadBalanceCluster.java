package lam.cobia.cluster;

import lam.cobia.rpc.Consumer;
import lam.cobia.rpc.Invocation;
import lam.cobia.rpc.Result;

import java.util.List;

/**
 * @description: RandomLoadBalanceCluster
 * @author: linanmiao
 * @date: 2018/7/22 0:30
 * @version: 1.0
 */
public class RandomLoadBalanceCluster<T> extends AbstractCluster<T>{

    public RandomLoadBalanceCluster(Class<T> interfaceClass, List<Consumer<T>> consumers) {
        super(interfaceClass, consumers);
    }

    @Override
    public Result doInvoke(Invocation invocation) {
        return null;
    }
}

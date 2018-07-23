package lam.cobia.loadbalance;

import lam.cobia.rpc.Consumer;
import lam.cobia.rpc.Invocation;

import java.util.List;

/**
 * @description: AbstractLoadBalance
 * @author: linanmiao
 * @date: 2018/7/23 23:44
 * @version: 1.0
 */
public abstract class AbstractLoadBalance implements LoadBalance{

    @Override
    public <T> Consumer<T> select(List<Consumer<T>> consumers, Invocation invocation) {
        Consumer<T> consumer = doSelect(consumers, invocation);
        return consumer;
    }

    public abstract <T> Consumer<T> doSelect(List<Consumer<T>> consumers, Invocation invocation);
}

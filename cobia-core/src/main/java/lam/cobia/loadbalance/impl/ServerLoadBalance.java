package lam.cobia.loadbalance.impl;

import lam.cobia.loadbalance.AbstractLoadBalance;
import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.support.Invocation;

import java.util.List;

/**
 * @description: ServerLoadBalance
 * @author: linanmiao
 * @date: 2019/1/20 0:58
 * @version: 1.0
 */
public class ServerLoadBalance extends AbstractLoadBalance {
    @Override
    public <T> Consumer<T> doSelect(List<Consumer<T>> consumers, Invocation invocation) {
        // TODO finish the work here.
        return null;
    }
}

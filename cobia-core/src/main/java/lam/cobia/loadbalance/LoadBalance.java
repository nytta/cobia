package lam.cobia.loadbalance;

import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.spi.Spiable;

import java.util.List;

/**
 * @description: LoadBalance
 * @author: linanmiao
 * @date: 2018/7/23 23:40
 * @version: 1.0
 */
@Spiable("random")
public interface LoadBalance {

    public <T> Consumer<T> select(List<Consumer<T>> consumers, Invocation invocation);

}

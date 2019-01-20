package lam.cobia.loadbalance.impl;

import lam.cobia.loadbalance.AbstractLoadBalance;
import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.support.Invocation;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description: ServerLoadBalance
 * @author: linanmiao
 * @date: 2019/1/20 0:58
 * @version: 1.0
 */
public class ServerLoadBalance extends AbstractLoadBalance {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLoadBalance.class);

    @Override
    public <T> Consumer<T> doSelect(List<Consumer<T>> consumers, Invocation invocation) {
        if (CollectionUtils.isEmpty(consumers)) {
            LOGGER.warn("[doSelect] invocation:{}, consumers is null or empty:{}", invocation, consumers);
            return null;
        }
        boolean serverBalanced = BooleanUtils.toBooleanDefaultIfNull(consumers.get(0).getRegistryData().getServiceBalanced(), false);
        for(Consumer<T> consumer : consumers) {
            if (serverBalanced) {

            }
        }
        // TODO finish the work here.
        return null;
    }
}

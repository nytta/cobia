package lam.cobia.loadbalance.impl;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.loadbalance.AbstractLoadBalance;
import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.support.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @description: ServerLoadBalance
 * @author: linanmiao
 * @date: 2019/1/20 0:58
 * @version: 1.0
 */
public class ServerLoadBalance extends AbstractLoadBalance {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLoadBalance.class);

    private static final Random RANDOM = new Random();

    @Override
    public <T> Consumer<T> doSelect(List<Consumer<T>> consumers, Invocation invocation) {
        if (CollectionUtils.isEmpty(consumers)) {
            LOGGER.warn("[doSelect] invocation:{}, consumers is null or empty:{}", invocation, consumers);
            return null;
        }
        long allWeight = 0L;
        for(Consumer<T> consumer : consumers) {
            allWeight += consumer.getRegistryData().getInvokedCount();
        }
        final long min = 1L;
        long randomLong = min + (long)(RANDOM.nextDouble() * (allWeight - min));
        for (Consumer<T> consumer : consumers) {
            randomLong -= consumer.getRegistryData().getInvokedCount();
            if (randomLong <= 0) {
                return consumer;
            }
        }
        LOGGER.debug("[doSelect] consumers:{}", GsonUtil.toNotNullJson(consumers));
        // cann't be happen.
        throw new CobiaException("Can select Consumer in " + ServerLoadBalance.class.getName());
    }
}

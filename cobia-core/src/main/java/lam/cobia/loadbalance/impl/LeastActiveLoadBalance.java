package lam.cobia.loadbalance.impl;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.loadbalance.AbstractLoadBalance;
import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.support.Invocation;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
public class LeastActiveLoadBalance extends AbstractLoadBalance {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeastActiveLoadBalance.class);

    private static final Random RANDOM = new Random();

    public LeastActiveLoadBalance() {
        super.name = "leastactive";
    }

    @Override
    public <T> Consumer<T> doSelect(List<Consumer<T>> consumers, Invocation invocation) {
        if (CollectionUtils.isEmpty(consumers)) {
            LOGGER.warn("[doSelect] invocation:{}, consumers is null or empty:{}", invocation, consumers);
            return null;
        }
        List<Integer> indexs = new ArrayList<>();
        long leastInvokedCount = Long.MAX_VALUE;
        for (int index = 0; index < consumers.size(); index++) {
            Consumer<T> consumer = consumers.get(index);
            if (BooleanUtils.isTrue(consumer.getRegistryData().getServiceBalanced())) {
                if (consumer.getRegistryData().getInvokedCount() < leastInvokedCount) {
                    leastInvokedCount = consumer.getRegistryData().getInvokedCount();
                    indexs.clear();
                    indexs.add(index);
                } else if (consumer.getRegistryData().getInvokedCount() == leastInvokedCount) {
                    // 有相同的invokedCount
                    indexs.add(index);
                }
            }
        }

        // cann't be happen.
        if (CollectionUtils.isEmpty(indexs)) {
            LOGGER.debug("[doSelect] consumers:{}", GsonUtil.toNotNullJson(consumers));
            throw new CobiaException("Can select Consumer in " + LeastActiveLoadBalance.class.getName());
        }

        if (indexs.size() > 1) {
            int randomIndex = RANDOM.nextInt(indexs.size());
            return consumers.get(randomIndex);
        }

        return consumers.get(indexs.get(0));
    }
}

package lam.cobia.loadbalance.impl;

import lam.cobia.core.util.ParamConstant;
import lam.cobia.loadbalance.AbstractLoadBalance;
import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.support.Invocation;

import java.util.List;
import java.util.Random;

/**
 * @description: RandomLoadBalance
 * @author: linanmiao
 * @date: 2018/7/23 23:46
 * @version: 1.0
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    public RandomLoadBalance() {
        super.name = "random";
    }

    @Override
    public <T> Consumer<T> doSelect(List<Consumer<T>> consumers, Invocation invocation) {
        int totalWeight = 0;
        for (Consumer<T> consumer : consumers) {
            totalWeight += consumer.getParamInt(ParamConstant.WEIGHT, ParamConstant.WEIGHT_DEFAULT) * 100;
        }
        int randomWeight = new Random().nextInt(totalWeight) + 1;
        for (Consumer<T> consumer : consumers) {
            randomWeight -= consumer.getParamInt(ParamConstant.WEIGHT, ParamConstant.WEIGHT_DEFAULT) * 100;
            if (randomWeight <= 0) {
                return consumer;
            }
        }
        //Shouldn't be happened.
        throw new IllegalStateException("Can not select Consumer<T>.");
    }
}

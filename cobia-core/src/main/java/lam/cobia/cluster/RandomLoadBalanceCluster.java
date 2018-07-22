package lam.cobia.cluster;

import lam.cobia.core.util.ParamConstant;
import lam.cobia.rpc.Consumer;
import lam.cobia.rpc.Invocation;
import lam.cobia.rpc.Result;

import java.util.List;
import java.util.Random;

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
        List<Consumer<T>> consumers = getConsumers();
        Consumer<T> consumer = select(consumers);
        Result result = consumer.invoke(invocation);
        return result;
    }

    private Consumer<T> select(List<Consumer<T>> consumers) {
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

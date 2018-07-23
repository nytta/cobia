package lam.cobia.cluster;

import lam.cobia.core.util.ParamConstant;
import lam.cobia.loadbalance.LoadBalance;
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
public class FailoverCluster<T> extends AbstractCluster<T>{

    private int retryTime = 2;

    public FailoverCluster(Class<T> interfaceClass, List<Consumer<T>> consumers, LoadBalance loadBalance) {
        super(interfaceClass, consumers, loadBalance);
    }

    @Override
    public Result doInvoke(Invocation invocation) {
        Consumer<T> selectedConsumer = null;
        Consumer<T> consumer = null;
        for (int invokeTime = 0; invokeTime <= retryTime; invokeTime++) {
            consumer = select(getConsumers(), selectedConsumer, invocation);
            try {
                selectedConsumer = consumer;
                Result result = consumer.invoke(invocation);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //shouldn't happen.
        throw new IllegalStateException("invoke fail");
    }

}

package lam.cobia.cluster;

import lam.cobia.core.model.RegistryData;
import lam.cobia.loadbalance.LoadBalance;
import lam.cobia.registry.Subcriber;
import lam.cobia.rpc.Consumer;
import lam.cobia.rpc.Invocation;
import lam.cobia.rpc.Result;

import java.util.List;

/**
 * @description: AbstractCluster
 * @author: linanmiao
 * @date: 2018/7/21 12:04
 * @version: 1.0
 */
public abstract class AbstractCluster<T> implements Cluster<T>, Subcriber {

    private Class<T> interfaceClass;

    private List<Consumer<T>> consumers;

    private LoadBalance loadBalance;

    public AbstractCluster(Class<T> interfaceClass, List<Consumer<T>> consumers, LoadBalance loadBalance) {
        this.interfaceClass = interfaceClass;
        if (consumers == null || consumers.isEmpty()) {
            throw new IllegalStateException("List<Consumer<T> consumers is null or empty.");
        }
        this.consumers = consumers;
        this.loadBalance = loadBalance;
    }

    @Override
    public String getKey() {
        return interfaceClass.getName();
    }

    @Override
    public RegistryData getRegistryData() {
        throw new IllegalStateException("Can invoke this method");
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public Result invoke(Invocation invocation) {
        return doInvoke(invocation);
    }

    @Override
    public void close() {
        for (Consumer<T> consumer : consumers) {
            consumer.close();
        }
    }

    protected List<Consumer<T>> getConsumers() {
        return this.consumers;
    }

    public abstract Result doInvoke(Invocation invocation);

    protected <T> Consumer<T> select(List<Consumer<T>> consumers, Consumer<T> selectedCosnumer, Invocation invocation) {
        if (consumers == null || consumers.isEmpty()) {
            return null;
        }
        Consumer<T> consumer = loadBalance.select(consumers, invocation);
        if (selectedCosnumer != null && consumer.equals(selectedCosnumer)) {
            consumers.remove(consumer);
            return select(consumers, selectedCosnumer, invocation);
        } else {
            return consumer;
        }
    }

    @Override
    public final void subscribe() {
        this.consumers.forEach((Consumer<T> consumer) -> {
            RegistryData registryData = consumer.getRegistryData();
            //reload consumer list
            //TODO
        });
    }

    private void invokeIllegal() {
        throw new IllegalStateException("This method can not be called.");
    }

    @Override
    public void setParam(String key, Object value) {
        invokeIllegal();
    }

    @Override
    public String getParam(String key) {
        invokeIllegal();
        return null;
    }

    @Override
    public String getParam(String key, String defaultValue) {
        invokeIllegal();
        return null;
    }

    @Override
    public int getParamInt(String key) {
        invokeIllegal();
        return 0;
    }

    @Override
    public int getParamInt(String key, int defaultValue) {
        invokeIllegal();
        return 0;
    }

    @Override
    public boolean getParamBoolean(String key) {
        invokeIllegal();
        return false;
    }

    @Override
    public boolean getParamBoolean(String key, boolean defaultValue) {
        invokeIllegal();
        return false;
    }
}

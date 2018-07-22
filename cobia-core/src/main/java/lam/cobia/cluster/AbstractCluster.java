package lam.cobia.cluster;

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
public abstract class AbstractCluster<T> implements Cluster<T>{

    private Class<T> interfaceClass;

    private List<Consumer<T>> consumers;

    public AbstractCluster(Class<T> interfaceClass, List<Consumer<T>> consumers) {
        if (consumers == null || consumers.isEmpty()) {
            throw new IllegalStateException("List<Consumer<T> consumers is null or empty.");
        }
        this.consumers = consumers;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public String getKey() {
        return interfaceClass.getName();
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

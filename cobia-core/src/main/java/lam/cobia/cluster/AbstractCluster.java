package lam.cobia.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.ParamConstant;
import lam.cobia.loadbalance.LoadBalance;
import lam.cobia.registry.Subcriber;
import lam.cobia.rpc.Consumer;
import lam.cobia.rpc.DefaultConsumer;
import lam.cobia.rpc.DefaultProtocol;
import lam.cobia.rpc.Invocation;
import lam.cobia.rpc.Protocol;
import lam.cobia.rpc.Result;
import lam.cobia.spi.ServiceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: AbstractCluster
 * @author: linanmiao
 * @date: 2018/7/21 12:04
 * @version: 1.0
 */
public abstract class AbstractCluster<T> implements Cluster<T> {

    private static Logger LOGGER = LoggerFactory.getLogger(AbstractCluster.class);

    private Class<T> interfaceClass;

    private List<Consumer<T>> consumers;

    private LoadBalance loadBalance;

    public AbstractCluster(Class<T> interfaceClass, List<Consumer<T>> consumers, LoadBalance loadBalance) {
        if (consumers == null || consumers.isEmpty()) {
            throw new IllegalStateException("List<Consumer<T> consumers is null or empty.");
        }
        this.interfaceClass = interfaceClass;
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

    public void reloadConsumers(String interfaceName, List<RegistryData> registryDatas) {
        LOGGER.info("[reloadConsumers] interface:{}, registryDatas:{}", interfaceName, registryDatas);
        synchronized (this) {
            final String spiName = "default";
            Protocol protocol = ServiceFactory.takeInstance(spiName, lam.cobia.rpc.Protocol.class);
            if (protocol == null) {
                throw new IllegalStateException("value of key:" + spiName + " in spi:" + lam.cobia.rpc.Protocol.class.getName() + " is null");
            }
            if (!(protocol instanceof DefaultProtocol)) {
                throw new IllegalStateException("value type: " + protocol + " of key:" + spiName
                                                + " in spi:" + lam.cobia.rpc.Protocol.class.getName() + " is not type of "
                                                + DefaultProtocol.class.getName());
            }
            DefaultProtocol defaultProtocol = (DefaultProtocol) protocol;
            List<Consumer<T>> consumerNewList = new ArrayList<>(this.consumers.size());
            boolean consumerExists;
            for (RegistryData registryData : registryDatas) {
                consumerExists = false;
                for (Consumer<T> consumer : this.consumers) {
                    if (consumer.getRegistryData().getHost().equals(registryData.getHost())) {
                        consumerExists = true;
                        consumerNewList.add(consumer);

                        LOGGER.info("[reloadConsumers] interface:{}, get old Consumer:{}", interfaceClass.getName(), consumer);
                        break ;
                    }
                }

                if (!consumerExists) {
                    //new provider
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put(ParamConstant.WEIGHT, registryData.getWeight());
                    Consumer<T> newConsumer = new DefaultConsumer<T>(interfaceClass, params, defaultProtocol.getClient(registryData), registryData);
                    consumerNewList.add(newConsumer);

                    LOGGER.info("[reloadConsumers] interface:{}, create new Consumer:{}", interfaceClass.getName(), newConsumer);
                }
            }
            this.consumers = consumerNewList;
        }
        LOGGER.info("[reloadConsumers] interface:{} end, new consumers:{}", interfaceName, this.consumers);
    }

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

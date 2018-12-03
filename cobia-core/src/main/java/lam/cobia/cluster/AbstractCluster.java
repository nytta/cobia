package lam.cobia.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.core.util.ParamConstant;
import lam.cobia.loadbalance.LoadBalance;
import lam.cobia.registry.RegistrySubcriber;
import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.DefaultConsumer;
import lam.cobia.rpc.DefaultProtocol;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.rpc.support.Protocol;
import lam.cobia.rpc.support.Result;
import lam.cobia.spi.ServiceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @description: AbstractCluster
 * @author: linanmiao
 * @date: 2018/7/21 12:04
 * @version: 1.0
 */
public abstract class AbstractCluster<T> implements Cluster<T>, RegistrySubcriber {

    private static Logger LOGGER = LoggerFactory.getLogger(AbstractCluster.class);

    private Class<T> interfaceClass;

    private List<Consumer<T>> consumers;

    private LoadBalance loadBalance;

    protected final String name;

    /**
     * should call setInterfaceClass, setConsumers, setLoadBalance method after AbstractCluster instance is created.
     */
    public AbstractCluster(String name) {
        this.name = name;
    }

    public AbstractCluster(String name, Class<T> interfaceClass, List<Consumer<T>> consumers, LoadBalance loadBalance) {
        if (consumers == null || consumers.isEmpty()) {
            throw new IllegalStateException("List<Consumer<T> consumers is null or empty.");
        }
        this.name = name;
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
        if (!interfaceName.equals(interfaceClass.getName())) {
            throw new IllegalArgumentException("need to reload consumers of interface:" + interfaceClass.getName() +
                                               ", but argument interfaceName:" + interfaceName);
        }
        LOGGER.info("[reloadConsumers] interface:{}, registryDatas:{}", interfaceName, registryDatas);
        synchronized (this) {
            final String spiName = "default";
            Protocol protocol = ServiceFactory.takeInstance(spiName, Protocol.class);
            if (protocol == null) {
                throw new IllegalStateException("value of key:" + spiName + " in spi:" + Protocol.class.getName() + " is null");
            }
            if (!(protocol instanceof DefaultProtocol)) {
                throw new IllegalStateException("value type: " + protocol + " of key:" + spiName
                                                + " in spi:" + Protocol.class.getName() + " is not type of "
                                                + DefaultProtocol.class.getName());
            }
            final List<Consumer<T>> copyConsumers = this.consumers;
            DefaultProtocol defaultProtocol = (DefaultProtocol) protocol;
            List<Consumer<T>> consumerNewList = new ArrayList<>(copyConsumers.size());
            boolean consumerExists;
            for (RegistryData registryData : registryDatas) {
                consumerExists = false;
                Iterator<Consumer<T>> iterator = copyConsumers.iterator();
                while (iterator.hasNext()) {
                    Consumer<T> consumer = iterator.next();
                    if (consumer.getRegistryData().getHost().equals(registryData.getHost())) {
                        consumerExists = true;
                        consumerNewList.add(consumer);

                        iterator.remove();

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

            //close invalid provider
            copyConsumers.forEach((Consumer<T> consumer) -> {
                try {
                    consumer.close();
                    LOGGER.info("[reloadConsumers] close invalid provider:{} success.", GsonUtil.toJson(consumer.getRegistryData()));
                } catch (Exception e) {
                    LOGGER.error("[reloadConsumers] close invalid provider:{} failed.", GsonUtil.toJson(consumer.getRegistryData()), e);
                }
            });

            this.consumers = consumerNewList;
        }
        LOGGER.info("[reloadConsumers] interface:{} end, new consumers:{}", interfaceName, this.consumers);
    }

    protected void reloadConsumer(String interfaceName, RegistryData registryData) {
        if (!interfaceName.equals(interfaceClass.getName())) {
            throw new IllegalArgumentException("need to reload consumer of interface:" + interfaceClass.getName() +
                                               ", but argument interfaceName:" + interfaceName);
        }
        LOGGER.info("[reloadConsumer] reload interface:{}, consumer:{}", interfaceName, registryData);
        synchronized (this) {
            final String spiName = "default";
            Protocol protocol = ServiceFactory.takeInstance(spiName, Protocol.class);
            if (protocol == null) {
                throw new IllegalStateException("value of key:" + spiName + " in spi:" + Protocol.class.getName() + " is null");
            }
            if (!(protocol instanceof DefaultProtocol)) {
                throw new IllegalStateException("value type: " + protocol + " of key:" + spiName
                                                + " in spi:" + Protocol.class.getName() + " is not type of "
                                                + DefaultProtocol.class.getName());
            }
            final List<Consumer<T>> copyConsumers = this.consumers;
            Iterator<Consumer<T>> iterator = copyConsumers.iterator();
            while (iterator.hasNext()) {
                Consumer<T> consumer = iterator.next();
                if (consumer.getRegistryData().getHost().equals(registryData.getHost())) {
                    iterator.remove();
                    consumer.close();
                    LOGGER.info("[reloadConsumer] interface:{}, get old Consumer:{}", interfaceClass.getName(), consumer);
                    break;
                }
            }

            //new provider
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(ParamConstant.WEIGHT, registryData.getWeight());
            DefaultProtocol defaultProtocol = (DefaultProtocol) protocol;
            Consumer<T> newConsumer = new DefaultConsumer<T>(interfaceClass, params, defaultProtocol.getClient(registryData), registryData);
            copyConsumers.add(newConsumer);

            this.consumers = copyConsumers;

            LOGGER.info("[reloadConsumer] interface:{}, create new Consumer:{}", interfaceClass.getName(), newConsumer);
        }
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

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setConsumers(List<Consumer<T>> consumers) {
        if (CollectionUtils.isEmpty(consumers)) {
            throw new IllegalArgumentException("List<Consumer<T>> consumers is null or empty.");
        }
        this.consumers = consumers;
    }

    public void setLoadBalance(LoadBalance loadBalance) {
        if (loadBalance == null) {
            throw new IllegalArgumentException("LoadBalance loadBalance is null");
        }
        this.loadBalance = loadBalance;
    }

    @Override
    public void setParam(String key, Object value) {
        throw new IllegalStateException("This method can not be called.");
    }

    @Override
    public String getParam(String key) {
        throw new IllegalStateException("This method can not be called.");
    }

    @Override
    public String getParam(String key, String defaultValue) {
        throw new IllegalStateException("This method can not be called.");
    }

    @Override
    public int getParamInt(String key) {
        throw new IllegalStateException("This method can not be called.");
    }

    @Override
    public int getParamInt(String key, int defaultValue) {
        throw new IllegalStateException("This method can not be called.");
    }

    @Override
    public boolean getParamBoolean(String key) {
        throw new IllegalStateException("This method can not be called.");
    }

    @Override
    public boolean getParamBoolean(String key, boolean defaultValue) {
        throw new IllegalStateException("This method can not be called.");
    }
}

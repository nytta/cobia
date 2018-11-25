package lam.cobia.registry.impl;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.core.util.ParamConstant;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.registry.AbstractRegistryProvider;
import lam.cobia.rpc.Provider;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @description: ZookeeperRegistryProvider
 * @author: linanmiao
 * @date: 2018/7/25 9:25
 * @version: 1.0
 */
public class ZookeeperRegistryProvider extends AbstractRegistryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistryProvider.class);

    private static final String ZOOKEEPER_ROOT_PATH = "/cobia";

    private ZkClient zkClient;

    public ZookeeperRegistryProvider() {
        super();
        initZkClient();
    }

    private void initZkClient() {
        if (this.zkClient != null) {
            return ;
        }
        final int connectionTimeout = Integer.MAX_VALUE;
        this.zkClient = new ZkClient(buildZkConnection(), connectionTimeout, new ZookeeperDefaultSerializer());
        LOGGER.info("[initZkClient] zkClient");
    }

    private ZkConnection buildZkConnection() {
        String address = ParameterUtil.getConfigParameter("address", getParamMap());
        if (StringUtils.isBlank(address)) {
            throw new CobiaException("registry type is zookeeper, but address of zookeeper is blank.");
        }
        int sessionTimeout = 5000;
        ZkConnection zkConnection = new ZkConnection(address, sessionTimeout);
        LOGGER.info("[buildZkConnection] build ZkConnection address:" + address + ", sessionTimeout:" + sessionTimeout + " success.");
        return zkConnection;
    }

    @Override
    public <T> boolean registry(Provider<T> provider, HostAndPort hap, Map<String, Object> params) {

        String inerfacePath = ZOOKEEPER_ROOT_PATH + "/" + provider.getInterface().getName();
        String path = inerfacePath + "/" + hap.getHost() + ":" + hap.getPort();

        try {
            zkClient.createPersistent(ZOOKEEPER_ROOT_PATH);
        } catch (Exception e) {
            if (e instanceof ZkNodeExistsException) {
                LOGGER.info("[registry] create persistent path " + ZOOKEEPER_ROOT_PATH + " to zookeeper, but path has exists.");
            } else {
                LOGGER.error("[registry] create persistent path " + provider.getInterface().getName() + " to zookeepr error");
                throw new CobiaException("create persistent path " + ZOOKEEPER_ROOT_PATH + " to zookeeper error", e);
            }
        }

        try {
            zkClient.createPersistent(inerfacePath);
        } catch (Exception e) {
            if (e instanceof ZkNodeExistsException) {
                LOGGER.info("[registry] create persistent path " + inerfacePath + " to zookeeper, but path has exists.");
            } else {
                LOGGER.error("[registry] registry provider " + provider.getInterface().getName() + " to zookeepr path " + inerfacePath + " error");
                throw new CobiaException("create persistent path " + inerfacePath + " to zookeeper error", e);
            }
        }
        int weight = ParameterUtil.getParameterInt(ParamConstant.WEIGHT, params, ParamConstant.WEIGHT_DEFAULT);

        RegistryData registryData = new RegistryData();
        registryData.setHost(hap.getHost());
        registryData.setPort(hap.getPort());
        registryData.setWeight(weight);

        Object data = GsonUtil.toJson(registryData);
        boolean nodeExists = false;
        try {
            path = zkClient.create(path, data, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            if (e instanceof ZkNodeExistsException) {
                nodeExists = true;
            } else {
                LOGGER.error("[registry] registry provider " + provider.getInterface().getName() + " to zookeepr error", e);
                throw new CobiaException("registry provider " + provider.getInterface().getName() + " to zookeepr path " + path + " error", e);
            }
        }
        if (!nodeExists) {
            LOGGER.info("[registry] registry provider " + provider.getInterface().getName() + " to zookeeper ephemeral path " + path
                    + " with data:" + data  + " success.");
        } else {
            boolean returnNullIfPathNotExists = true;
            String oldData = zkClient.readData(path, returnNullIfPathNotExists);
            LOGGER.warn("[registry] registry provider " + provider.getInterface().getName() + " to zookeeper path " + path
                        + ", but path has exists, data:" + oldData);
        }
        return !nodeExists;
    }

    @Override
    public <T> void onProviderDataChanges(Provider<T> provider, RegistryData registryData) {
        final String path = String.format("%s/%s/%s:%d",
                ZOOKEEPER_ROOT_PATH, provider.getInterface().getName(), registryData.getHost(), registryData.getPort());
        String data = GsonUtil.toJson(registryData);
        try {
            zkClient.writeData(path, data);
            LOGGER.debug("[onProviderDataChanges] interface:{}, RegistryData:{}, config of provider update to registry center success."
                    , provider.getInterface().getName(), registryData);
        } catch (Exception e) {
            LOGGER.error("[onProviderDataChanges] Provider:{}, RegistryData:{}", provider, registryData);
        }
    }
}

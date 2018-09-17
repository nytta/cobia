package lam.cobia.registry.impl;

import lam.cobia.config.spring.CRegistryBean;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.core.util.ParamConstant;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.registry.AbstractRegistryProvider;
import lam.cobia.registry.RegistryProvider;
import lam.cobia.rpc.Provider;
import lam.cobia.spi.ServiceFactory;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
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
    }

    private void initZkClient() {
        if (this.zkClient != null) {
            return ;
        }
        final int connectionTimeout = Integer.MAX_VALUE;
        this.zkClient = new ZkClient(buildZkConnection(), connectionTimeout, buildZkSerializer());
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

    private ZkSerializer buildZkSerializer() {
        ZkSerializer zkSerializer = new ZkSerializer() {
            String charsetName = "utf-8";
            @Override
            public byte[] serialize(Object data) throws ZkMarshallingError {
                try {
                    return ((String)data).getBytes(charsetName);
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("[buildZkSerializer] unsupport encoding " + charsetName, e);
                    return null;
                }
            }

            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                try {
                    return new String(bytes, charsetName);
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("[buildZkSerializer] unsupport encoding " + charsetName, e);
                    return null;
                }
            }
        };
        LOGGER.info("[buildZkSerializer] build ZkSerializer success.");
        return zkSerializer;
    }

    @Override
    public <T> boolean registry(Provider<T> provider, HostAndPort hap, Map<String, Object> params) {
        initZkClient();

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
}

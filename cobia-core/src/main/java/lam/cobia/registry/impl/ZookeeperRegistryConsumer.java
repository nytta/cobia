package lam.cobia.registry.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import lam.cobia.config.spring.CRegistryBean;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.registry.AbstractRegistryConsumer;
import lam.cobia.registry.RegistryConsumer;
import lam.cobia.spi.ServiceFactory;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * @description: ZookeeperRegistryConsumer
 * @author: linanmiao
 * @date: 2018/7/25 9:42
 * @version: 1.0
 */
public class ZookeeperRegistryConsumer extends AbstractRegistryConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistryConsumer.class);

    private static final String ZOOKEEPER_ROOT_PATH = "/cobia";

    private ZkClient zkClient;

    public ZookeeperRegistryConsumer() {
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
    public <T> List<HostAndPort> getProviders(Class<T> interfaceClass) {
        initZkClient();

        String interfacePath = ZOOKEEPER_ROOT_PATH + "/" + interfaceClass.getName();

        List<String> addresses = zkClient.getChildren(interfacePath);
        LOGGER.info("[getProviders] get provider list of interface " + interfaceClass.getName() + ", list:" + addresses);

        if (CollectionUtils.isEmpty(addresses)) {
            return new ArrayList<>();
        }
        List<HostAndPort> list = new ArrayList<HostAndPort>(addresses.size());
        for (String address : addresses) {
            String host;
            int port;
            int index = address.indexOf(":");
            if (index == -1) {
                host = address;
                port = 80;
            } else {
                String[] strs = address.split(":");
                host = strs[0];
                port = Integer.parseInt(strs[1]);
            }
            HostAndPort hap = new HostAndPort().setHost(host).setPort(port);
            list.add(hap);
        }
        LOGGER.info("[getProviders] provider HostAndPort list:" + list);
        return list;
    }
}

package lam.cobia.registry.impl;

import java.util.ArrayList;
import java.util.List;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.registry.AbstractRegistryConsumer;
import lam.cobia.registry.RegistrySubcriber;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
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
    public <T> List<RegistryData> getProviders(Class<T> interfaceClass, RegistrySubcriber registrySubcriber) {
        if (registrySubcriber == null) {
            throw new IllegalArgumentException("RegistrySubcriber registrySubcriber is null.");
        }

        String interfacePath = ZOOKEEPER_ROOT_PATH + "/" + interfaceClass.getName();

        //provider address list
        List<String> addresses = zkClient.getChildren(interfacePath);

        LOGGER.info("[getProviders] get provider list of interface " + interfaceClass.getName() + ", list:" + addresses);

        if (CollectionUtils.isEmpty(addresses)) {
            return new ArrayList<>();
        }

        //subscribes provider-list changes of interface provider
        zkClient.subscribeChildChanges(interfacePath, (String parentPath, List<String> currentChilds) -> {
            LOGGER.info("[subscribeChildChanges] path:{}, current children:{}", parentPath, currentChilds);
            List<RegistryData> registryDatas = new ArrayList<>();
            currentChilds.forEach((String child) -> {
                String childPath = parentPath + "/" + child;
                String childData = zkClient.readData(childPath);

                LOGGER.info("[subscribeChildChanges] path:{}, data:{}", childPath, childData);

                RegistryData registryData = GsonUtil.fromJson(childData, RegistryData.class);
                registryDatas.add(registryData);
            });
            registrySubcriber.onProvidersChanges(interfaceClass, registryDatas);
        });

        List<RegistryData> list = new ArrayList<RegistryData>(addresses.size());
        for (String address : addresses) {
            String data = zkClient.readData(interfacePath + "/" + address);
            RegistryData registryData = GsonUtil.fromJson(data, RegistryData.class);
            list.add(registryData);

            zkClient.subscribeDataChanges(interfacePath + "/" + address, new IZkDataListener() {
                @Override
                public void handleDataChange(String s, Object o) throws Exception {
                    LOGGER.info("[subscribeDataChanges] {}:{}", s, o);
                    //TODO reload provider config with registrySubcribe.onProviderChanges method.
                }

                @Override
                public void handleDataDeleted(String s) throws Exception {

                }
            });
        }
        LOGGER.info("[getProviders] provider RegistryData list:" + list + " of interface " + interfaceClass.getName());
        return list;
    }
}

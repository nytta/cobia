package lam.cobia.registry.impl;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.ParamConstant;
import lam.cobia.registry.AbstractRegistryConsumer;
import lam.cobia.registry.RegistrySubcriber;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: DirectRegistryConsumer
 * @author: linanmiao
 * @date: 2018/7/26 0:20
 * @version: 1.0
 */
public class DirectRegistryConsumer extends AbstractRegistryConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectRegistryConsumer.class);

    public DirectRegistryConsumer() {
        super();
    }

    @Override
    public <T> List<RegistryData> getProviders(Class<T> interfaceClass, RegistrySubcriber registrySubcriber) {
        //get server info from tag <registry /> .
        final String addressKey = "address";
        if (!getParamMap().containsKey(addressKey)) {
            throw new CobiaException(getClass().getName() + " do not contains param key: " + addressKey);
        }
        String address = getParamMap().get(addressKey).toString();
        if (StringUtils.isBlank(address)) {
            throw new CobiaException("key: " + addressKey + " can't be blank in " + getClass().getName());
        }
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

        List<RegistryData> list = new ArrayList<RegistryData>();
        RegistryData hap = new RegistryData();
        hap.setHost(host);
        hap.setPort(port);
        hap.setWeight(ParamConstant.WEIGHT_DEFAULT);
        list.add(hap);

        LOGGER.debug("[getProviders] interface:{}, provider list:{}", interfaceClass.getName(), list);

        return list;
    }
}

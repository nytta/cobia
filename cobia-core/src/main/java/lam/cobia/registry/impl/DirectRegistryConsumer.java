package lam.cobia.registry.impl;

import lam.cobia.config.spring.CRegistryBean;
import lam.cobia.core.constant.Constant;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.registry.AbstractRegistryConsumer;
import lam.cobia.registry.RegistryConsumer;
import lam.cobia.spi.ServiceFactory;
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
    public <T> List<HostAndPort> getProviders(Class<T> interfaceClass) {
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

        List<HostAndPort> list = new ArrayList<HostAndPort>();
        HostAndPort hap = new HostAndPort().setHost(host).setPort(port);
        list.add(hap);

        LOGGER.info("[getProviders] interface:{}, provider list:{}", interfaceClass.getName(), list);

        return list;
    }
}

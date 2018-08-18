package lam.cobia.registry.impl;

import lam.cobia.core.constant.Constant;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.registry.RegistryConsumer;
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
public class DirectRegistryConsumer implements RegistryConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectRegistryConsumer.class);

    @Override
    public <T> List<HostAndPort> getProviders(Class<T> interfaceClass) {
        String serverHost;
        int port;
        //TODO
        //get server info from tag <registry /> in the future.
        String serviceServer = ParameterUtil.getParameter("serviceServer",
                String.format("%s:%d", Constant.DEFAULT_SERVER_HOSTNAME, Constant.DEFAULT_SERVER_PORT));
        if (StringUtils.isBlank(serviceServer)) {
            throw new IllegalArgumentException("attribute serviceServer can't be null when attribute registry is direct.");
        }
        int index = serviceServer.indexOf(":");
        if (index == -1) {
            serverHost = serviceServer;
            port = 80;
        } else {
            String[] strs = serviceServer.split(":");
            serverHost = strs[0];
            port = Integer.parseInt(strs[1]);
        }

        List<HostAndPort> list = new ArrayList<HostAndPort>();
        HostAndPort hap = new HostAndPort().setHost(serverHost).setPort(port);
        list.add(hap);

        LOGGER.info("[getProviders] interface:{}, provider list:{}", interfaceClass.getName(), list);

        return list;
    }
}

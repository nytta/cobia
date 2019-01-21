package lam.cobia.rpc;

import lam.cobia.cluster.AbstractCluster;
import lam.cobia.config.spring.CRefrenceBean;
import lam.cobia.config.spring.CRegistryBean;
import lam.cobia.config.spring.CServiceBean;
import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.NetUtil;
import lam.cobia.core.util.ParamConstant;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lam.cobia.cluster.FailoverCluster;
import lam.cobia.core.constant.Constant;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.loadbalance.LoadBalance;
import lam.cobia.remoting.ChannelHandler;
import lam.cobia.remoting.Client;
import lam.cobia.remoting.DefaultChannelHanlder;
import lam.cobia.remoting.ExchangeServer;
import lam.cobia.remoting.HeaderExchangeServer;
import lam.cobia.remoting.transport.netty.NettyClient;
import lam.cobia.remoting.transport.netty.NettyServer;
import lam.cobia.rpc.chain.ProviderChainWrapper;
import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.support.Exporter;
import lam.cobia.rpc.support.Protocol;
import lam.cobia.rpc.support.Provider;
import lam.cobia.spi.ServiceFactory;
import org.apache.commons.lang3.BooleanUtils;

/**
* <p>
* default protocol
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class DefaultProtocol implements Protocol {

	private final Object sharedObject = new Object();
	
	private final ConcurrentMap<Consumer<?>, Object> consumerMap = new ConcurrentHashMap<>();
	
	private final ConcurrentMap<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();
	
	private final ConcurrentMap<String, ExchangeServer> serverMap = new ConcurrentHashMap<>();
	
	private final ConcurrentMap<Integer, lam.cobia.remoting.transport.netty.NettyServer> nettyMap = new ConcurrentHashMap<>();
	
	private final ConcurrentMap<InetSocketAddress, lam.cobia.remoting.Client> clientsMap = new ConcurrentHashMap<>();
	
	public DefaultProtocol() {
	}

	@Override
	public <T> Exporter<T> export(Provider<T> provider, CServiceBean<T> serviceBean) {
		boolean balanced  = BooleanUtils.toBooleanDefaultIfNull(serviceBean.getBalanced(), false);
		ProviderChainWrapper<T> providerChainWrapper = balanced ? new BalancedProvider<T>(provider) : new ProviderChainWrapper<>(provider, null);

	    DefaultExporter<T> exporter = new DefaultExporter<T>(providerChainWrapper);

	    exporterMap.put(provider.getKey(), exporter);
	    
	    openServer(provider);

		final int port = ParameterUtil.getParameterInt(Constant.KEY_PORT, Constant.DEFAULT_SERVER_PORT);
		HostAndPort hap = new HostAndPort().setHost(NetUtil.getLocalHost()).setPort(port);
	    //do work: registry provider
		CRegistryBean.getRegistryProvider().registry(provider, hap, serviceBean.getParams());
	    
		return exporter;
	}

	@Override
	public <T> Consumer<T> refer(Class<T> clazz, CRefrenceBean<T> refrenceBean) {

		LoadBalance loadBalance = ServiceFactory.takeInstance(refrenceBean.getLoadbalance(), LoadBalance.class);
		// TODO 2019-01-21 need to check that whether provider supports special loadbalance mode.

		AbstractCluster<T> cluster = new FailoverCluster<T>();

		//get provider list of interface:clazz, and registry subcriber to consumer client.
		List<RegistryData> list = CRegistryBean.getRegistryConsumer().getProviders(clazz, cluster);

		List<Consumer<T>> consumers = new ArrayList<Consumer<T>>();
		for (RegistryData registryData : list) {
			refrenceBean.getParams().put(ParamConstant.WEIGHT, registryData.getWeight());
			Consumer<T> consumer = new DefaultConsumer<T>(clazz, refrenceBean.getParams(), getClient(registryData), registryData);
			consumerMap.put(consumer, sharedObject);
			consumers.add(consumer);
		}

		cluster.setInterfaceClass(clazz);
		cluster.setConsumers(consumers);
		cluster.setLoadBalance(loadBalance);

		return cluster;
	}
	
	private void openServer(Provider<?> provider) {
		ExchangeServer server = serverMap.get(provider.getKey());
		if (server == null) {
			serverMap.putIfAbsent(provider.getKey(), createServer(provider));
		}
	}
	
	private ExchangeServer createServer(Provider<?> provider) {		
		int port = ParameterUtil.getParameterInt(Constant.KEY_PORT, Constant.DEFAULT_SERVER_PORT);

		NettyServer nettyServer = nettyMap.get(port); 
		if (nettyServer == null) {
			ChannelHandler channelHandler = new DefaultChannelHanlder(exporterMap);
			nettyServer = new NettyServer(port, channelHandler);
			nettyMap.put(port, nettyServer);
		} else {
			nettyServer.reset();			
		}
		
		Objects.requireNonNull(nettyServer, "NettyServer is null.");
		HeaderExchangeServer server = new HeaderExchangeServer(nettyServer);
		
		return server;
	}
	
	public Client getClient(RegistryData hap) {
		String serverHost = hap.getHost();
		int port = hap.getPort();
		InetSocketAddress remoteAddress = new InetSocketAddress(serverHost, port);
		boolean clientIsShare = ParameterUtil.getParameterBoolean(Constant.KEY_CLIENT_IS_SHARE, Constant.DEFAULT_CLIENT_IS_SHARE);
		if (clientIsShare) {
			Client client = clientsMap.get(new InetSocketAddress(serverHost, port));
			if (client == null) {
				client = new NettyClient(remoteAddress);
				clientsMap.putIfAbsent(remoteAddress, client);
			}
			return client;
		}
		return new NettyClient(remoteAddress);
	}

	@Override
	public void close() {
		for (Consumer<?> consumer : consumerMap.keySet()) {
			consumer.close();
		}
		for (Exporter<?> exporter : exporterMap.values()) {
			exporter.close();
		}
	}

}

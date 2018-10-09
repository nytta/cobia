package lam.cobia.rpc;

import lam.cobia.cluster.AbstractCluster;
import lam.cobia.config.spring.CRegistryBean;
import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.NetUtil;
import lam.cobia.core.util.ParamConstant;
import lam.cobia.registry.RegistryConsumer;
import lam.cobia.registry.RegistryProvider;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lam.cobia.cluster.Cluster;
import lam.cobia.cluster.FailoverCluster;
import lam.cobia.cluster.FailoverCluster;
import lam.cobia.core.constant.Constant;
import lam.cobia.core.model.HostAndPort;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.loadbalance.LoadBalance;
import lam.cobia.registry.Subcriber;
import lam.cobia.registry.ZookeeperSubcriber;
import lam.cobia.registry.impl.ZookeeperRegistryConsumer;
import lam.cobia.remoting.ChannelHandler;
import lam.cobia.remoting.Client;
import lam.cobia.remoting.DefaultChannelHanlder;
import lam.cobia.remoting.ExchangeServer;
import lam.cobia.remoting.HeaderExchangeServer;
import lam.cobia.remoting.transport.netty.NettyClient;
import lam.cobia.remoting.transport.netty.NettyServer;
import lam.cobia.spi.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* <p>
* default protocol
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class DefaultProtocol implements Protocol{

	private final Object sharedObject;
	
	private ConcurrentMap<Consumer<?>, Object> consumerMap;
	
	private ConcurrentMap<String, Exporter<?>> exporterMap;
	
	private ConcurrentMap<String, ExchangeServer> serverMap;
	
	private ConcurrentMap<Integer, lam.cobia.remoting.transport.netty.NettyServer> nettyMap;
	
	private ConcurrentMap<InetSocketAddress, lam.cobia.remoting.Client> clientsMap;
	
	public DefaultProtocol() {
		this.sharedObject = new Object();
		this.consumerMap = new ConcurrentHashMap<Consumer<?>, Object>();
		this.exporterMap = new ConcurrentHashMap<String, Exporter<?>>();
		this.serverMap = new ConcurrentHashMap<String, ExchangeServer>();
		this.nettyMap = new ConcurrentHashMap<Integer, lam.cobia.remoting.transport.netty.NettyServer>();
		this.clientsMap = new ConcurrentHashMap<InetSocketAddress, lam.cobia.remoting.Client>();
	}

	
	@Override
	public <T> Exporter<T> export(Provider<T> provider, Map<String, Object> params) {

	    DefaultExporter<T> exporter = new DefaultExporter<T>(provider, provider.getKey());

	    exporterMap.put(provider.getKey(), exporter);
	    
	    openServer(provider);

		String host = NetUtil.getLocalHost();
		int port = ParameterUtil.getParameterInt(Constant.KEY_PORT, Constant.DEFAULT_SERVER_PORT);
		HostAndPort hap = new HostAndPort().setHost(host).setPort(port);
	    //do work: registry provider
		CRegistryBean.getRegistryProvider().registry(provider, hap, params);
	    
		return exporter;
	}

	@Override
	public <T> Consumer<T> refer(Class<T> clazz, Map<String, Object> params) {

		//registry subcriber to consumer client.
		Subcriber subcriber = null;
		if (CRegistryBean.getRegistryConsumer() instanceof ZookeeperRegistryConsumer) {
			subcriber = new ZookeeperSubcriber();
			((ZookeeperRegistryConsumer)CRegistryBean.getRegistryConsumer()).setSubcriber(subcriber);
		}

		//get provider list of interface:clazz
		List<RegistryData> list = CRegistryBean.getRegistryConsumer().getProviders(clazz);

		List<Consumer<T>> consumers = new ArrayList<Consumer<T>>();
		for (RegistryData registryData : list) {
			params.put(ParamConstant.WEIGHT, registryData.getWeight());
			Consumer<T> consumer = new DefaultConsumer<T>(clazz, params, getClient(registryData), registryData);
			consumerMap.put(consumer, sharedObject);
			consumers.add(consumer);
		}
		LoadBalance loadBalance = ServiceFactory.takeDefaultInstance(LoadBalance.class);
		AbstractCluster<T> cluster = new FailoverCluster<T>(clazz, consumers, loadBalance);

		//to set consumer cluster, let it can be reload consumer when provider is updated.
		if (CRegistryBean.getRegistryConsumer() instanceof ZookeeperRegistryConsumer) {
			((ZookeeperSubcriber) subcriber).setAbstractCluster(cluster);
		}

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

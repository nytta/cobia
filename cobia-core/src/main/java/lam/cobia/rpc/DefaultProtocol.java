package lam.cobia.rpc;

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
import lam.cobia.remoting.ChannelHandler;
import lam.cobia.remoting.Client;
import lam.cobia.remoting.DefaultChannelHanlder;
import lam.cobia.remoting.ExchangeServer;
import lam.cobia.remoting.HeaderExchangeServer;
import lam.cobia.remoting.transport.netty.NettyClient;
import lam.cobia.remoting.transport.netty.NettyServer;
import lam.cobia.spi.ServiceFactory;

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
	public <T> Exporter<T> export(Provider<T> provider) {

	    DefaultExporter<T> exporter = new DefaultExporter<T>(provider, provider.getKey());

	    exporterMap.put(provider.getKey(), exporter);
	    
	    openServer(provider);
	    
		return exporter;
	}

	@Override
	public <T> Consumer<T> refer(Class<T> clazz, Map<String, Object> params) {
		//create DefaultInvoker<T> object with tcp client[]

		//Class<T> clazz ->  List<HostAndPort>
		List<Consumer<T>> consumers = new ArrayList<Consumer<T>>();
		List<HostAndPort> list = getHostAndPorts(clazz, params);
		for (HostAndPort hap : list) {
			Consumer<T> consumer = new DefaultConsumer<T>(clazz, params, getClient(hap));
			consumerMap.put(consumer, sharedObject);
			consumers.add(consumer);
		}
		LoadBalance loadBalance = ServiceFactory.takeDefaultInstance(LoadBalance.class);
		Cluster<T> cluster = new FailoverCluster<T>(clazz, consumers, loadBalance);
		return cluster;
	}

	private List<HostAndPort> getHostAndPorts(Class<?> clazz, Map<String, Object> params) {
		//server host and port temporarily,
		//It will registry by zookeeper in the future.
		//@TODO
		String serverHost = ParameterUtil.getParameter(Constant.KEY_SERVER_HOST, Constant.DEFAULT_SERVER_HOSTNAME);
		int port = ParameterUtil.getParameterInt(Constant.KEY_PORT, Constant.DEFAULT_SERVER_PORT);

		String registry = ParameterUtil.getConfigParameter("registry", params, "zookeeper");
		if ("direct".equals(registry)) {
			String serviceServer = ParameterUtil.getConfigParameter("serviceServer", params);
			if (StringUtils.isBlank(serviceServer)) {
				throw new IllegalArgumentException("attribute serviceServer cann't be null when attribute registry is direct.");
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
		}

		List<HostAndPort> list = new ArrayList<HostAndPort>();
		HostAndPort hap = new HostAndPort()
				.setHost(serverHost).setPort(port);
		list.add(hap);
		return list;
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
	
	private Client getClient(HostAndPort hap) {
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

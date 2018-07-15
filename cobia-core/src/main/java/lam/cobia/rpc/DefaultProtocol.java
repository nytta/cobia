package lam.cobia.rpc;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lam.cobia.core.constant.Constant;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.remoting.ChannelHandler;
import lam.cobia.remoting.Client;
import lam.cobia.remoting.DefaultChannelHanlder;
import lam.cobia.remoting.ExchangeServer;
import lam.cobia.remoting.HeaderExchangeServer;
import lam.cobia.remoting.transport.netty.NettyClient;
import lam.cobia.remoting.transport.netty.NettyServer;

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

	//private ConcurrentMap<Invoker<?>, Object> invokerMap;
	
	private ConcurrentMap<Consumer<?>, Object> consumerMap;
	
	private ConcurrentMap<String, Exporter<?>> exporterMap;
	
	private ConcurrentMap<String, ExchangeServer> serverMap;
	
	private ConcurrentMap<Integer, lam.cobia.remoting.transport.netty.NettyServer> nettyMap;
	
	private ConcurrentMap<InetSocketAddress, lam.cobia.remoting.Client[]> clientsMap;
	
	public DefaultProtocol() {
		this.sharedObject = new Object();
		//this.invokerMap = new ConcurrentHashMap<Invoker<?>, Object>();
		this.consumerMap = new ConcurrentHashMap<Consumer<?>, Object>();
		this.exporterMap = new ConcurrentHashMap<String, Exporter<?>>();
		this.serverMap = new ConcurrentHashMap<String, ExchangeServer>();
		this.nettyMap = new ConcurrentHashMap<Integer, lam.cobia.remoting.transport.netty.NettyServer>();
		this.clientsMap = new ConcurrentHashMap<InetSocketAddress, lam.cobia.remoting.Client[]>();
	}

	/*private static class DefaultProtocolHolder {
		private static DefaultProtocol INSTANCE = new DefaultProtocol(); 
	}
	
	public static DefaultProtocol getInstance() {
		return DefaultProtocolHolder.INSTANCE;
	}*/
	
	@Override
	public <T> Exporter<T> export(Provider<T> provider) {

	    DefaultExporter<T> exporter = new DefaultExporter<T>(provider, provider.getKey());

	    exporterMap.put(provider.getKey(), exporter);
	    
	    openServer(provider);
	    
		return exporter;
	}

	@Override
	public <T> Consumer<T> refer(Class<T> clazz) {
		//create DefaultInvoker<T> object with tcp client[]
		String serverHost = ParameterUtil.getParameter(Constant.KEY_SERVER_HOST, Constant.DEFAULT_SERVER_HOSTNAME);
		int port = ParameterUtil.getParameterInt(Constant.KEY_PORT, Constant.DEFAULT_SERVER_PORT);
		
		//DefaultInvoker<T> invoker = new DefaultInvoker<T>(clazz, getClients(clazz));
		//ProtoBufInvoker<T> invoker = new ProtoBufInvoker<T>(clazz, getClients(serverHost, port));
		//invokerMap.put(invoker, sharedObject);
		Consumer<T> consumer = new DefaultConsumer<T>(clazz, getClients(serverHost, port));
		consumerMap.put(consumer, sharedObject);
		return consumer;
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
	
	private Client[] getClients(String serverHost, int port) {
		InetSocketAddress remoteAddress = new InetSocketAddress(serverHost, port);
		boolean clientIsShare = ParameterUtil.getParameterBoolean(Constant.KEY_CLIENT_IS_SHARE, Constant.DEFAULT_CLIENT_IS_SHARE);
		if (clientIsShare) {
			Client[] clients = clientsMap.get(new InetSocketAddress(serverHost, port));
			if (clients == null) {
				NettyClient client = new NettyClient(remoteAddress);
				clients = new Client[] {client};
				clientsMap.putIfAbsent(remoteAddress, clients);
			}
			return clients;
		}
		return new Client[] {new NettyClient(remoteAddress)};
	}

	@Override
	public void close() {
		/*for (Invoker<?> invoker : invokerMap.keySet()) {
			invoker.close();
		}*/
		for (Consumer<?> consumer : consumerMap.keySet()) {
			consumer.close();
		}
		for (Exporter<?> exporter : exporterMap.values()) {
			exporter.close();
		}
	}

}

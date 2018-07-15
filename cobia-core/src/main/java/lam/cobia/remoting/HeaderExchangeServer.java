package lam.cobia.remoting;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Objects;

import lam.cobia.remoting.transport.netty.NettyChannel;

/**
* <p>
* default ExchangeServer class
* </p>
* @author linanmiao
* @date 2018年1月2日
* @version 1.0
*/
public class HeaderExchangeServer implements ExchangeServer{
	
	private final Server server;
	
	public HeaderExchangeServer(Server server) {
		this.server = Objects.requireNonNull(server, "server cann't be null!");
		if (this.server.isClose()) {
			throw new IllegalStateException("server is closed.");
		}
	}

	@Override
	public int getPort() {
		return server.getPort();
	}

	@Override
	public void close() {
		server.close();
	}

	@Override
	public boolean isClose() {
		return server.isClose();
	}

	@Override
	public Collection<NettyChannel> getChannels() {
		Collection<NettyChannel> channels = server.getChannels();
		return channels;
	}

	@Override
	public NettyChannel getChannel(InetSocketAddress remoteAddress) {
		Collection<NettyChannel> channels = server.getChannels();
		for (NettyChannel channel : channels) {
			//..
			
		}
		return null;
	}

	@Override
	public void reset() {
		this.server.reset();
	}

}

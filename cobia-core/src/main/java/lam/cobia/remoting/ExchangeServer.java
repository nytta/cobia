package lam.cobia.remoting;

import java.net.InetSocketAddress;
import java.util.Collection;

import lam.cobia.remoting.transport.netty.NettyChannel;

/**
* <p>
* exchange server
* </p>
* @author linanmiao
* @date 2018年1月2日
* @version 1.0
*/
public interface ExchangeServer extends Server{
	
	public Collection<NettyChannel> getChannels();
	
	public NettyChannel getChannel(InetSocketAddress remoteAddress);

}

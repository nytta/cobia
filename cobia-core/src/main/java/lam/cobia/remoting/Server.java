package lam.cobia.remoting;

import java.io.Closeable;
import java.util.Collection;

import lam.cobia.remoting.transport.netty.NettyChannel;

/**
* <p>
* server
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public interface Server extends Closeable, Resetable{
	
	public int getPort();
	
	public Collection<NettyChannel> getChannels();
	
	@Override
	public void close();
	
	public boolean isClose();

}

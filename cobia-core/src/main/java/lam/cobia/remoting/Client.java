package lam.cobia.remoting;

import java.io.Closeable;
import java.net.InetSocketAddress;

/**
* <p>
* client
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public interface Client extends Closeable{
	
	public InetSocketAddress getServerAddress();
	
	public Channel getChannel();
	
	public void close();
	
	public boolean isClose();

}

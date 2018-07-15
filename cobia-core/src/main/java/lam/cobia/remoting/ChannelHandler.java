package lam.cobia.remoting;

/**
* <p>
* channel handler
* </p>
* @author linanmiao
* @date 2018年1月2日
* @version 1.0
*/
public interface ChannelHandler {
	
	public void received(Channel channel, Object msg);

}

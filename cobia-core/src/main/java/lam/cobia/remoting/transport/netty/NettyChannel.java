package lam.cobia.remoting.transport.netty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lam.cobia.remoting.Channel;
import lam.cobia.remoting.codec.CobiaPacket;
import lam.cobia.serialize.support.CobiaSerializer;
import lam.cobia.spi.ServiceFactory;

/**
* <p>
* netty channel
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class NettyChannel implements Channel{
	
	private io.netty.channel.Channel channel;
	
	private static ConcurrentMap<io.netty.channel.Channel, lam.cobia.remoting.Channel> channelMap = 
			new ConcurrentHashMap<io.netty.channel.Channel, lam.cobia.remoting.Channel>();
	
	public CobiaSerializer serializer = ServiceFactory.takeDefaultInstance(CobiaSerializer.class);//ProtobufSerializer.getInstance();
	
	public NettyChannel(io.netty.channel.Channel channel) {
		this.channel = channel;
	}
	
	public static Channel getChannel(io.netty.channel.Channel channel) {
		Channel ch = channelMap.get(channel);
		if (ch == null) {
			channelMap.putIfAbsent(channel, new NettyChannel(channel));
			ch = channelMap.get(channel);
		}
		return ch;
	}
	
	public static void remove(io.netty.channel.Channel channel) {
		channelMap.remove(channel);
	}

	@Override
	public void send(Object msg) {
		Class<?> clazz = msg.getClass();

	    byte[] data = serializer.serialize(msg, clazz);
	    
	    CobiaPacket packet = CobiaPacket.newPacket(data);
	    
		channel.writeAndFlush(packet);
		
	}

}

package lam.cobia.remoting.transport.netty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.log.Console;
import lam.cobia.remoting.Channel;
import lam.cobia.remoting.IBody;
import lam.cobia.remoting.codec.CobiaPacket;
import lam.cobia.serialize.ProtobufSerializer;
import lam.cobia.serialize.support.CobiaSerializer;

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
	
	//public CobiaSerializer serializer = Hessian2Serializer.getInstance();
	
	public CobiaSerializer serializer = ProtobufSerializer.getInstance();
	
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
		Console.println("class: " + clazz + ", msg:" + msg);
		
		if (msg instanceof IBody) {
			IBody body = (IBody) msg;
			try {
				//参数对象类型
				Class<?> dataClass = Class.forName(body.getDataClassName());
				//参数对象
				byte[] dataBytes = serializer.serialize(body.getData(), dataClass);
				body.setData(dataBytes);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new CobiaException(e);
			}
		} else {
			throw new CobiaException("not support class:" + clazz.getName());
		}
		
		/* if (msg instanceof Request2) {
			Request2 request2 = (Request2) msg;
			Class<?> dataClass;
			try {
				//提供者方法的参数对象类型
				dataClass = Class.forName(request2.getDataClassName());
				//提供者方法的参数对象
				byte[] dataBytes = serializer.serialize(request2.getData(), dataClass);
				request2.setData(dataBytes);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new CobiaException(e);
			}
		} else if (msg instanceof Response2){
			Response2 response2 = (Response2) msg;
			response2.getDataClassName();
			Class<?> dataClass;
			try {
				//提供者方法的参数对象类型
				dataClass = Class.forName(response2.getDataClassName());
				//提供者方法的参数对象
				byte[] dataBytes = serializer.serialize(response2.getData(), dataClass);
				response2.setData(dataBytes);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new CobiaException(e);
			}
		} else {

			//do nothing.
		} */

	    byte[] data = serializer.serialize(msg, clazz);
	    
	    CobiaPacket packet = CobiaPacket.newPacket(data);
	    
		channel.writeAndFlush(packet);
		
	}

}

package lam.cobia.remoting.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
* <p>
* codec for tcp data
* </p>
* @author linanmiao
* @date 2018年4月27日
* @version 1.0
*/
public interface Codec {
	
	public void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf byteBuf) throws Exception;
	
	public void decode();

}

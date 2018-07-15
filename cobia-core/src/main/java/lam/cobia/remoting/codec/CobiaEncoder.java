package lam.cobia.remoting.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lam.cobia.core.exception.CobiaException;

/**
* <p>
* Cobia encoder.
* </p>
* @author linanmiao
* @date 2018年4月27日
* @version 1.0
*/
public class CobiaEncoder extends MessageToByteEncoder<Packet> /*implements Codec*/{

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf byteBuf) throws Exception {
		//sorted: mark|length|data
		if (packet.getLength() < 0) {
			throw new CobiaException("length can not be negative:" + packet.getLength());
		}
		if (packet.getData() == null) {
			throw new CobiaException("field byte[] data in Packet instance packet is null");
		}
		if (packet.getLength() != packet.getData().length) {
			throw new CobiaException("length isn't equal to the length of data");
		}
		
		//mark of head
		byteBuf.writeInt(packet.getMark());
		//length of data in head
		byteBuf.writeInt(packet.getLength());
		if (packet.getLength() > 0) {
			//data
			byteBuf.writeBytes(packet.getData());
		}
	}

}

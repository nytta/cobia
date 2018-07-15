package lam.cobia.remoting.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年4月27日
* @version 1.0
*/
public class CobiaDecoder extends ByteToMessageDecoder {
	
	private final int LEAST_LENGTH = 8; //mark(4 byte) + length(4 byte)

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= LEAST_LENGTH) {
			//跳过太大的字节流
			if (in.readableBytes() >= 2048) {
				in.skipBytes(in.readableBytes());
			}
			
			//包头部的标记
			int beginIndex;
			while (true) {
				beginIndex = in.readerIndex();
				in.markReaderIndex();
				if (in.readInt() == CobiaPacket.getDefaultMark()) {
					//找到字节流的头部开头标记
					break ;
				}
				//跳过不是头部开头的标记
				in.resetReaderIndex();
				in.readByte();
				
				//字节长度过短，则等待后面的包过来，比较拆包了，先收到一个包，需要再等下一个包过来，这里先退出。
				if (in.readableBytes() < LEAST_LENGTH) {
					return ;
				}
			}
			
			int length = in.readInt();
			//头部，异常的长度值
			if (length < 0) {
				length = 0;
			}
			if (in.readableBytes() < length) {
				//还原读指针
				in.readerIndex(beginIndex);
				//如果数据长度还没达到，先退出，等待数据过来。
				return ;
			}
			byte[] data = CobiaPacket.newBytes(length);
			if (length > 0) {
				//读取length个字节的数据到data中
				in.readBytes(data);
			}
			
			Packet packet = CobiaPacket.newPacket(data);
			out.add(packet);
		}
	}

}

package lam.cobia.remoting.transport.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lam.cobia.core.util.NetUtil;
import lam.cobia.log.Console;
import lam.cobia.remoting.ChannelHandler;
import lam.cobia.remoting.Request2;
import lam.cobia.remoting.codec.Packet;
import lam.cobia.serialize.ProtobufDeserializer;
import lam.cobia.serialize.support.CobiaDeserializer;
import lam.cobia.spi.ServiceFactory;

/**
* <p>
* netty server handler
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	//key:<host:port>
	private final ConcurrentMap<String, NettyChannel> channelMap = new ConcurrentHashMap<String, NettyChannel>();
	
	private ChannelHandler handler;
	
	public CobiaDeserializer deserializer = ServiceFactory.takeDefaultInstance(CobiaDeserializer.class);//ProtobufDeserializer.getInstance();
	
	public NettyServerHandler(ChannelHandler handler) {
		this.handler = handler;
	}
	
	public ConcurrentMap<String, NettyChannel> getChannels() {
		return channelMap;
	}
	
	@Override
	public boolean isSharable() {
		return true;//super.isSharable();
	}
	
    /**
     * Calls {@link ChannelHandlerContext#fireChannelRegistered()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelUnregistered()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	Channel channel = ctx.channel();
    	String key = NetUtil.parseToString((InetSocketAddress) channel.remoteAddress());
    	if (channelMap.get(key) == null) {
    		channelMap.putIfAbsent(key, new NettyChannel(channel));
    		Console.println("add channel:" + channel + ", key:" + key);
    	}
        //handle client connect event...
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelInactive()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	Channel channel = ctx.channel();
    	String key = NetUtil.parseToString((InetSocketAddress) channel.remoteAddress());
    	channelMap.remove(key);
    	//handle client disconnect event...
    	Console.println("remove channel:" + channel + ", key:" + key);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ctx.fireChannelRead(msg);
    	io.netty.channel.Channel channel = ctx.channel();
    	Console.println(channel + " receive >>>" + msg);
    	String key = NetUtil.parseToString((InetSocketAddress) channel.remoteAddress());
    	lam.cobia.remoting.Channel ch = channelMap.get(key);
    	
    	Packet packet = (Packet) msg;
    	if (packet.getLength() > 0) {
	    	//Request request = deserializer.deserialize(packet.getData(), Request.class);
    		Request2 request2 = deserializer.deserialize(packet.getData(), Request2.class);
	    	handler.received(ch, request2);
    	}
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelReadComplete()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //ctx.fireChannelReadComplete();
    	ctx.channel().flush();
    }

    /**
     * Calls {@link ChannelHandlerContext#fireUserEventTriggered(Object)} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //ctx.fireUserEventTriggered(evt);
    	super.userEventTriggered(ctx, evt);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelWritabilityChanged()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        //ctx.fireChannelWritabilityChanged();
    	super.channelWritabilityChanged(ctx);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	Channel channel = ctx.channel();
    	String key = NetUtil.parseToString((InetSocketAddress) channel.remoteAddress());
    	Console.println("exceptionCaught channel:" + channel + ", key:" + key);
        ctx.fireExceptionCaught(cause);
        //handle client error event...
    }
}

package lam.cobia.remoting.transport.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.remoting.Channel;
import lam.cobia.remoting.codec.CobiaDecoder;
import lam.cobia.remoting.codec.CobiaEncoder;
import lam.cobia.remoting.transport.AbstractClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* <p>
* netty client
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class NettyClient extends AbstractClient{

	private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);
	
	private EventLoopGroup group;
	
	private io.netty.channel.Channel channel;
	
	public NettyClient(InetSocketAddress remoteAddress) {
		super(remoteAddress);
	}

	@Override
	public void onOpen() {
		group = new NioEventLoopGroup();
		
		try {
			Bootstrap client = new Bootstrap()
			.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline cp = ch.pipeline();
					cp.addLast(new CobiaEncoder());
					cp.addLast(new CobiaDecoder());
					cp.addLast(new NettyClientHandler());
				}
			});
			
			ChannelFuture future = client.connect(getServerAddress()).sync();
			// Wait until the connection is closed.
			channel = future.channel();
		} catch (Exception e) {
			LOGGER.error("[onOpen] failed", e);
			throw new CobiaException(e);
		}
	}

	@Override
	public void onClose() {
		try {
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			LOGGER.error("[onClose] failed", e);
		} finally {
			group.shutdownGracefully();
		}
	}
	
	@Override
	public Channel getChannel() {
		if (channel == null || !channel.isActive()) {
			return null;
		}
		return NettyChannel.getChannel(channel);
	}
	
}

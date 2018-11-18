package lam.cobia.remoting.transport.netty;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.remoting.ChannelHandler;
import lam.cobia.remoting.codec.CobiaDecoder;
import lam.cobia.remoting.codec.CobiaEncoder;
import lam.cobia.remoting.transport.AbstractServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* <p>
* netty server
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class NettyServer extends AbstractServer{

	private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);
	
	private EventLoopGroup bossGroup;
	
	private EventLoopGroup workerGroup;
	
	//key:<host:port>
	private ConcurrentMap<String, NettyChannel> channelMap;
	
	public NettyServer(int port, ChannelHandler handler) { 
		super(port, handler);
	}

	@Override
	public void onOpen() {
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();
		
		final NettyServerHandler nettyServerHandler = new NettyServerHandler(handler);
		channelMap = nettyServerHandler.getChannels();
		
		try {
			ServerBootstrap server = new ServerBootstrap()
			.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.WARN))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline cp = ch.pipeline();
					cp.addLast(new CobiaEncoder());
					cp.addLast(new CobiaDecoder());
					cp.addLast(nettyServerHandler);
				}
			});
			/*ChannelFuture future = */server.bind(getPort()).sync();
			//Channel channel = future.channel();
			// Wait until the server socket is closed.
			//channel.closeFuture().sync();
		} catch (Exception e) {
			LOGGER.error("[onOpen] failed", e);
			throw new CobiaException(e);
		}
	}

	@Override
	public void onClose() {
		// Shut down all event loops to terminate all threads.
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
	
	@Override
	public Collection<NettyChannel> getChannels() {
		Set<NettyChannel> channels = new HashSet<NettyChannel>(channelMap.size());
		for (NettyChannel nettyChannel : channelMap.values()) {
			channels.add(nettyChannel);
		}
		return channels;
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}

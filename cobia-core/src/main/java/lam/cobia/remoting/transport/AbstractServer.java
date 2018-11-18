package lam.cobia.remoting.transport;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.remoting.ChannelHandler;
import lam.cobia.remoting.Server;

/**
* <p>
* abstract server class
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public abstract class AbstractServer implements Server{
	
	private AtomicBoolean closed = new AtomicBoolean();
	
	private final int port;
	
	private static Logger logger = LoggerFactory.getLogger(AbstractClient.class);
	
	protected ChannelHandler handler;

	public AbstractServer(final int port, ChannelHandler handler) {
		this.port = port;
		this.handler = Objects.requireNonNull(handler);
		if (this.port <= 0) {
			throw new IllegalArgumentException("server port " + this.port + " can be negative.");
		}
		try {
			onOpen();
			logger.info("server open on port " + port + " success.");
		} catch(Exception e) {
			logger.error("open server fail, port:" + port);
			System.exit(0);
		}
	}
	
	@Override
	public int getPort() {
		return port;
	}
	
	@Override
	public final void close() {
		//to guarantee the operation is idempotent, it's not a good idea to use "!closed.getAndSet(true)"
		boolean oldValue = closed.get();
		if (!oldValue && closed.compareAndSet(oldValue, true)) {
			onClose();
		}
	}
	
	@Override
	public boolean isClose() {
		return closed.get();
	}
	
	public abstract void onOpen();
	
	public abstract void onClose();
	
}

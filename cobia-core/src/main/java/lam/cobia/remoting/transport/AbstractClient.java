package lam.cobia.remoting.transport;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.util.NetUtil;
import lam.cobia.remoting.Client;

/**
* <p>
* abstract client class
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public abstract class AbstractClient implements Client{

	private AtomicBoolean closed = new AtomicBoolean();
	
	private final InetSocketAddress remoteAddress;
	
	private static Logger logger = LoggerFactory.getLogger(AbstractClient.class);
	
	public AbstractClient(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
		if (this.remoteAddress == null) {
			throw new IllegalArgumentException("remoteAddress of server is null");
		}
		try {
			onOpen();
			logger.debug("client connect to " + NetUtil.parseToString(remoteAddress) + " success.");
		} catch (Exception e) {
			logger.error("client open connect to server[" + NetUtil.parseToString(remoteAddress) + "] fail.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	@Override
	public final void close() {
		//to guarantee the operation is idempotent
		boolean oldValue = closed.get();
		if (!oldValue && closed.compareAndSet(oldValue, true)) {
			onClose();
		}
	}
	
	@Override
	public InetSocketAddress getServerAddress() {
		return remoteAddress;
	}
	
	@Override
	public boolean isClose() {
		return closed.get();
	}
	
	public abstract void onOpen();
	
	public abstract void onClose();
	
}

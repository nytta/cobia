package lam.cobia.remoting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lam.cobia.core.constant.Constant;
import lam.cobia.core.exception.CobiaException;

/**
* <p>
* default future
* </p>
* @author linanmiao
* @date 2017年12月22日
* @version 1.0
*/
public class DefaultFuture implements ResponseFuture{
	
	private final IRequest request;
	
	private final Channel channel;
	
	private volatile IResponse response;
	
	private Lock lock;
	
	private Condition requestDone;
	
	private static final ConcurrentMap<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<Long, DefaultFuture>();
	
	public DefaultFuture(IRequest request, Channel channel) {
		this.request = request;
		this.channel = channel;
		this.lock = new ReentrantLock();
		this.requestDone = lock.newCondition();
		FUTURES.put(request.getId(), this);
	}
	
	Channel getChannel() {
		return channel;
	}
	
	public static void received(Channel channel, IResponse response) {
		DefaultFuture future = FUTURES.remove(response.getId());
		if (future != null) {
			future.doReceived(response);
		} else {
			throw new CobiaException("Can not find the DefaultFuture object for request id:" + response.getId());
		}
	}
	
	private void doReceived(IResponse response) {
		lock.lock();
		try {
			this.response = response;
			requestDone.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	private boolean isDone() {
		return this.response != null;
	}

	@Override
	public Object get() {
		return get(Constant.DEFAULT_TIMEOUT);
	}

	@Override
	public Object get(long milliseconds) {
		if (!isDone()) {
			lock.lock();
			try {
				if (!isDone()) {					
					try {
						boolean notTimeout = requestDone.await(milliseconds, TimeUnit.MILLISECONDS);
						if (!notTimeout) {
							throw new CobiaException("time out(" + milliseconds + "ms) when waiting for response id:" + request.getId());
						}
					} catch (InterruptedException e) {
						throw new CobiaException("Occurs error when waiting for response id:" + request.getId(), e);
					}
				}
			} finally {
				lock.unlock();
			}
		}
		return response;
	}

}

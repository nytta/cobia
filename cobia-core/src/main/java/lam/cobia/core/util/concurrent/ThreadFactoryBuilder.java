package lam.cobia.core.util.concurrent;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
* <p>
* Just like class name say.
* </p>
* @author linanmiao
* @date 2017年4月11日
* @versio 1.0
*/
public class ThreadFactoryBuilder {
	
	private String threadNamePrefix;
	private boolean daemon = Boolean.FALSE.booleanValue();//default value
	private int priority = Thread.NORM_PRIORITY;    //default
	
	public ThreadFactoryBuilder setThreadNamePrefix(String threadNamePrefix){
		this.threadNamePrefix = threadNamePrefix;
		return this;
	}
	
	public ThreadFactoryBuilder setDaemon(boolean daemon) {
		this.daemon = daemon;
		return this;
	}
	
	public ThreadFactoryBuilder setPriority(int priority) {
		this.priority = priority;
		return this;
	}
	
	public ThreadFactory build(){
		return new GenericThreadFactory(threadNamePrefix, daemon, priority);
	}
	
	private static class GenericThreadFactory implements ThreadFactory{

		private static final AtomicLong nFactorys = new AtomicLong(0L);
		private final AtomicLong nThreads = new AtomicLong(0L);
		private final ThreadGroup threadGroup;
		private final String namePrefix;
		private boolean daemon;
		private int priority;
		
		public GenericThreadFactory(String threadNamePrefix){
			this(threadNamePrefix, Boolean.FALSE.booleanValue(), Thread.NORM_PRIORITY);
		}
		
		public GenericThreadFactory(String threadNamePrefix, boolean daemon, int priority) {
			Objects.requireNonNull(threadNamePrefix, "threadNamePrefix must not be null.");
			this.daemon = daemon;
			this.priority = priority;
			SecurityManager securityManager = System.getSecurityManager();
			threadGroup = securityManager == null ? 
					Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
			namePrefix = String.format("%s-%d-thread-", threadNamePrefix, nFactorys.incrementAndGet());
		}
		
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(threadGroup, r, namePrefix + nThreads.incrementAndGet(), 0);
			/*if(t.isDaemon()){
				t.setDaemon(Boolean.FALSE.booleanValue());
			}*/
			t.setDaemon(daemon);
			/*if(t.getPriority() != Thread.NORM_PRIORITY){
				t.setPriority(Thread.NORM_PRIORITY);
			}*/
			t.setPriority(priority);
			return t;
		}
		
	}

}

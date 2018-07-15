package lam.cobia.log;

import java.io.Closeable;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import lam.cobia.core.util.SystemProperties;
import lam.cobia.core.util.concurrent.ThreadFactoryBuilder;

/**
* <p>
* Set system property "lam.log.isAsyn" to be "true", then Appender will be asyn mode.
* </p>
* @author linanmiao
* @date 2017年11月21日
* @version 1.0
*/
public class LamLAppender implements LAppender, Closeable{
	
	private final boolean isAsyn;
	
	private volatile boolean isClosed;
	
	private PrintStream out;
	
	private LinkedBlockingQueue<String> logQueue;
	
	private ScheduledThreadPoolExecutor logScheduler;
	
	public LamLAppender() {
		this.isAsyn = SystemProperties.getPropertyBoolean("lam.log.isAsyn");
		this.out = System.out;

		initAsynInfo();
	}
	
	private void initAsynInfo() {
		this.logQueue = new LinkedBlockingQueue<String>();
		this.logScheduler = new ScheduledThreadPoolExecutor(
				1,
				new ThreadFactoryBuilder().setThreadNamePrefix("LamLAppender").build(),
				new ThreadPoolExecutor.AbortPolicy());
		this.logScheduler.scheduleWithFixedDelay(new Runnable(){
			@Override
			public void run() {
				List<String> list = new LinkedList<String>();
				int len = logQueue.drainTo(list);
				for (int i = 0; i < len; i++) {
					print(list.get(i));
				}
			}
		}, 0, 10, TimeUnit.MILLISECONDS);		
	}
	
	private void print(String s) {
		out.print(s);
	}

	@Override
	public LAppender append(String log) {
		if (isClosed) {
			print(LamLAppender.class.getName() + " isClosed, can't append log message any more.");
			return this;
		}
		if (!isAsyn) {
			print(log);
			return this;
		}
		logQueue.offer(log);
		return this;
	}

	@Override
	public void close() {
		isClosed = true;
		if (logScheduler != null) {
			logScheduler.shutdown();
		}
	}

}

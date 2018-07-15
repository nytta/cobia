package lam.cobia.log;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Appender;
import org.apache.log4j.AsyncAppender;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.cobia.core.util.concurrent.ThreadFactoryBuilder;

/**
* <p>
* 定时异步日志
* </p>
* @author linanmiao
* @date 2017年8月17日
* @version 1.0
*/
public class LamScheduleAsyncAppender extends AsyncAppender implements AppenderAttachable {
	
	private final static Set<LamScheduleAsyncAppender> set = new HashSet<LamScheduleAsyncAppender>();
	
	private final LinkedBlockingQueue<LoggingEvent> logQueue;
	
	private final ScheduledThreadPoolExecutor scheduleExecutor;
	
	private volatile boolean shutdown;
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				Iterator<LamScheduleAsyncAppender> iter = set.iterator();
				while(iter.hasNext())
					iter.next().close();
			}
		});
	}
	
	/**
	 * Nested appenders.
	 */
	private final AppenderAttachableImpl appenders;
	
	public LamScheduleAsyncAppender(){
		this(80, TimeUnit.MILLISECONDS);
	}
	
	public LamScheduleAsyncAppender(long period, TimeUnit timeUnit){
		logQueue = new LinkedBlockingQueue<LoggingEvent>();
		appenders = new AppenderAttachableImpl();
		scheduleExecutor = new ScheduledThreadPoolExecutor(
				1, 
				new ThreadFactoryBuilder().setThreadNamePrefix("LamSchedule").build(),
				new ScheduleRejectedHandle());
		scheduleExecutor.scheduleAtFixedRate(new Dispatcher(), 0, period, timeUnit);
		set.add(this);
	}
	
	@Override
	public void append(LoggingEvent event) {
		if(shutdown)
			return ;
		logQueue.offer(event);
	}
	
	@Override
	public void close() {
		if(shutdown)
			return ;
		shutdown = true;
		System.out.println(getClass().getName() + " is closing.");
		super.close();
		
		scheduleExecutor.shutdown();
		
		consumeLoggingEvent();
		Appender appender;
		Enumeration<?> e = appenders.getAllAppenders();
		while(e.hasMoreElements()){
			appender = (Appender) e.nextElement();
			appender.close();
		}
		System.out.println(getClass().getName() + " is closed.");
	}
	
	private class Dispatcher implements Runnable{

		@Override
		public void run() {
			if(shutdown)
				return ;
			consumeLoggingEvent();
		}
	}
	
	/**
	 * 消费日志
	 */
	private void consumeLoggingEvent(){
		List<LoggingEvent> list = new LinkedList<LoggingEvent>();
		logQueue.drainTo(list);
		Iterator<LoggingEvent> iter = list.iterator();
		while(iter.hasNext()){
			appenders.appendLoopOnAppenders(iter.next());
		}		
	}
	
	private static class ScheduleRejectedHandle implements RejectedExecutionHandler{
		private static Logger logger = LoggerFactory.getLogger(ScheduleRejectedHandle.class);

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			logger.warn("reject runnable:" + r);
		}
	}
	
	/**
    Add an appender.
	 */
	public void addAppender(Appender newAppender){
		synchronized (appenders) {			
			appenders.addAppender(newAppender);
		}
	}

	 /**
	    Get all previously added appenders as an Enumeration.  */
	 public Enumeration<?> getAllAppenders(){
		 synchronized (appenders) {
			 return appenders.getAllAppenders();
		}
	 }

	 /**
	    Get an appender by name.
	  */
	 public Appender getAppender(String name){
		synchronized (appenders) {
			return appenders.getAppender(name);
		}
	 }

 
	 /**
	    Returns <code>true</code> if the specified appender is in list of
	    attached attached, <code>false</code> otherwise.
	
	    @since 1.2 */
	 public  boolean isAttached(Appender appender){
		synchronized (appender) {
			return appenders.isAttached(appender);
		}
	 }

	 /**
	    Remove all previously added appenders.
	 */
	public void removeAllAppenders(){
		synchronized (appenders) {
			appenders.removeAllAppenders();
		}
	 }


	 /**
	    Remove the appender passed as parameter from the list of appenders.
	 */
	 public void removeAppender(Appender appender){
		synchronized (appenders) {
			appenders.removeAppender(appender);
		}
	 }


	/**
	   Remove the appender with the name passed as parameter from the
	   list of appenders.  
	 */
	public void removeAppender(String name){
		synchronized (appenders) {
			appenders.removeAppender(name);
		}
	}

}

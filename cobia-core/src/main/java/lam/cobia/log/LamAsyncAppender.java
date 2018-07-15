package lam.cobia.log;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.log4j.AsyncAppender;
import org.apache.log4j.spi.LoggingEvent;

import lam.cobia.log.filter.LamAsyncFilter;

/**
* <p>
* async appender for log
* </p>
* @author linanmiao
* @date 2017年8月17日
* @version 1.0
*/
public class LamAsyncAppender extends AsyncAppender{
	
	//private static Gson gson = new Gson();
	private static Field bufferField;
	
	static{
		try {
			Class<?> clazz = Class.forName(AsyncAppender.class.getName());
			bufferField = clazz.getDeclaredField("buffer");
			if(!bufferField.isAccessible())
				bufferField.setAccessible(Boolean.TRUE.booleanValue());
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} catch (NoSuchFieldException e) {
			System.err.println(e);
		} catch (SecurityException e) {
			System.err.println(e);
		} catch (IllegalArgumentException e) {
			System.err.println(e);
		}
	}
	
	public LamAsyncAppender(){
		super();
		init();
		System.out.println(getClass().getName() + " constructor");
	}
	
	private void init(){
		super.addFilter(new LamAsyncFilter());
	}
	
	@Override
	public void append(LoggingEvent event) {
		//List<?> buffer = getParentBuffer();
		
		//int previousSize = buffer.size();
		super.append(event);
		//int currentSize = buffer.size();
		//System.out.println(String.format("size[previous:%d, current:%d]", previousSize, currentSize));
	}
	
	private List<?> getParentBuffer(){
		try {
			return (List<?>) bufferField.get(this);
		} catch (IllegalArgumentException e) {
			System.err.println(e);
		} catch (IllegalAccessException e) {
			System.err.println(e);
		}
		return null;
	}
}

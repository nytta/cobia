package lam.cobia.spi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

import org.apache.commons.lang3.StringUtils;

import lam.cobia.core.exception.CobiaException;

/**
* <p>
* cobia<br/>
* like:<br/>
* META-INF/services/com.example.CodecSet<br/>
* since JDK1.6.
* </p>
* @author linanmiao
* @date 2018年7月7日
* @versio 1.0
*/
public class ServiceFactory {
	
	private static final String PREFIX = "META-INF/services/";
	
	private static Map<Class<?>, Map<String, Object>> map = new WeakHashMap<Class<?>, Map<String, Object>>();
	
	/*public static <T> T build(Class<T> clazz) {
		ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
		Iterator<T> iter = serviceLoader.iterator();
		while (iter.hasNext()) {
			T t = iter.next();
			return t;
		}
		throw new IllegalStateException("Can not find instance of " + clazz.getName() + ".");
	}*/
	
	public static <T> T takeDefaultInstance(Class<T> serviceTypeClass) {
		Spiable spi = checkAndGet(serviceTypeClass);
		
		String defaultName = spi.value();
		if (StringUtils.isBlank(defaultName)) {
			throw new CobiaException(serviceTypeClass.getName() + " hasn't default value on annotation " + Spiable.class.getName() + ".");
		}
		return takeInstance(defaultName, serviceTypeClass);
	}
	
	private static <T> Spiable checkAndGet(Class<T> serviceTypeClass) {
		if (!serviceTypeClass.isInterface()) {
			throw new CobiaException(serviceTypeClass.getName() + " is not an interface.");
		}
		
		Spiable spi = serviceTypeClass.getAnnotation(Spiable.class);
		if (spi == null) {
			throw new CobiaException(serviceTypeClass.getName() + " does not has annoattion:" + Spiable.class.getName() + ".");
		}
		return spi;
	}
	
	public static <T> T takeInstance(String name, Class<T> serviceTypeClass) {
		checkAndGet(serviceTypeClass);
		
		Map<String, Object> m = map.get(serviceTypeClass);
		if (m == null) {
			m = new HashMap<String, Object>();
			map.put(serviceTypeClass, m);
		}
		Object o = m.get(name);
		if (o != null) {
			return (T) o;
		}
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String fullname = PREFIX + serviceTypeClass.getName();
		if (classLoader == null) {
			classLoader = ClassLoader.getSystemClassLoader();	
		}
		
		T t = null;

		try {
			Enumeration<URL> configs = classLoader.getResources(fullname);
			boolean isFileExists = false;
			while (configs.hasMoreElements()) {
				isFileExists = true;
				URL url = configs.nextElement();
				InputStream inStream = url.openStream();
				Properties p = new Properties();
				try {
					p.load(inStream);
				} catch (IOException e) {
					throw new CobiaException(e);
				} catch (IllegalArgumentException ei) {
					throw new CobiaException(ei);
				} finally {
					if (inStream != null) {
						inStream.close();
					}
				}
				
				
				String className = p.getProperty(name);
				
				if (StringUtils.isBlank(className)) {
					throw new CobiaException("Can't find key:" + name + " in file:" + fullname);
				}
				
				Class<?> c = null;
				try {
					c = Class.forName(className);
				} catch (ClassNotFoundException e) {
					throw new CobiaException("Can't find class:" + className, e);
				}
				if (!serviceTypeClass.isAssignableFrom(c)) {
					throw new CobiaException(c.getName() + " is not type of class:" + className);
				}
				
				try {
					t = (T) c.newInstance();
					
					m.put(name, t);
					return t;
				} catch (InstantiationException e) {
					throw new CobiaException(e);
				} catch (IllegalAccessException e) {
					throw new CobiaException(e);
				}
			}
			
			if (!isFileExists) {
				//This can't be happened. 
				throw new CobiaException("Can not find file:" + fullname);
			}
			
			//This can't be happened. 
			throw new CobiaException("config value of " + name + " in class " + serviceTypeClass.getName()
					+ " is not found in file:" + fullname);
		} catch (IOException e) {
			throw new CobiaException(e);
		}
	}

}

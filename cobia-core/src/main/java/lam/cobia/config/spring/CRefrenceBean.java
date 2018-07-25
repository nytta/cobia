package lam.cobia.config.spring;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lam.cobia.log.Console;
import lam.cobia.spi.ServiceFactory;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年6月20日
* @versio 1.0
*/
public class CRefrenceBean<T> extends AbstractConfig
	    implements InitializingBean, DisposableBean, ApplicationContextAware, FactoryBean<T>{

	private static final long serialVersionUID = -5581695019639111046L;
	
	private String id;
	
	private String interfaceName;
	
	private Class<?> interfaceClass;
	
	private volatile T ref;

	private String registry = "zookeeper"; //default registry:zookeeper
	
	private final AtomicBoolean refInited = new AtomicBoolean(false);
	
	private ApplicationContext applicationContext;
	
	//======================
	
	//The method of implementing interface org.springframework.beans.factory.InitializingBean
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	//The method of implementing interface org.springframework.beans.factory.DisposableBean
	@Override
	public void destroy() throws Exception {
		
	}
	
	//The method of implementing interface org.springframework.context.ApplicationContextAware
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	//The method of implementing interface org.springframework.beans.factory.FactoryBean
	@Override
	public T getObject() throws Exception {
		T t = getRef();
		Console.println("interface:" + interfaceClass + ", ref hashCode:" + System.identityHashCode(t));
		return t;
	}
	
	//The method of implementing interface org.springframework.beans.factory.FactoryBean
	@Override
	public Class<?> getObjectType() {
		return this.interfaceClass;
	}
	
	//The method of implementing interface org.springframework.beans.factory.FactoryBean
	@Override
	public boolean isSingleton() {
		return true;
	}
	
	//======================
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setInterface(String interfaceName) {
		this.interfaceName = interfaceName;
		if (this.interfaceClass == null) {
			try {
				Class<?> clazz = Class.forName(interfaceName);
				setInterface(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new IllegalStateException("Can't find class:" + interfaceName, e);
			}
		}
	}
	
	public void setInterface(Class<?> interfaceClass) {
		Objects.requireNonNull(interfaceClass, "setInterface(Class<?> interfaceClass) param is null");
		this.interfaceClass = interfaceClass;
	}
	
	public T getRef() {
		Objects.requireNonNull(interfaceClass, "interfaceClass is null");
		boolean oldValue = refInited.get();
		if (!oldValue && refInited.compareAndSet(oldValue, true)) {
			//@TODO init ref
			ref = (T) ServiceFactory.takeDefaultInstance(Reference.class).refer(interfaceClass);//(T) CobiaReference.getInstance().refer(interfaceClass);
		}
		return ref;
	}

	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}
}

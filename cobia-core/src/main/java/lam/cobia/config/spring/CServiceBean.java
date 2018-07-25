package lam.cobia.config.spring;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

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
public class CServiceBean<T> extends AbstractConfig 
		implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener<ApplicationEvent>, BeanNameAware{

	private static final long serialVersionUID = 4263813570880386858L;
	
	public ApplicationContext applicationContext;
	
	private String interfaceName;
	
	private Class<?> interfaceClass;
	
	private T ref;
	
	private String beanName;
	
	private String id;

	private String registry = "zookeeper"; //default registry:zookeeper
	
	//=============
	
	//The method of implementing of interface org.springframework.beans.factory.InitializingBean
	@Override
	public void afterPropertiesSet() throws Exception {
		//初始化完bean的属性值之后
		Console.println(toString());
	}
	
	//The method of implementing of interface org.springframework.beans.factory.DisposableBean
	@Override
	public void destroy() throws Exception {
		//销毁bean之前
		
	}
	
	//The method of implementing of interface org.springframework.context.ApplicationContextAware
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	//The method of implementing of interface org.springframework.context.ApplicationListener
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		//1.org.springframework.context.event.ContextRefreshedEvent ->
		//2.org.springframework.context.event.ContextStartedEvent ->
		//3.org.springframework.context.event.ContextClosedEvent
		Console.println(event.getClass().getName());
		if (ContextRefreshedEvent.class.getName().equals(event.getClass().getName())) {
			//do export bean
			ServiceFactory.takeDefaultInstance(Service.class).export(getRef(), (Class<T>) interfaceClass);
		}
	}
	
	//The method of implementing of interface org.springframework.beans.factory.BeanNameAware
	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
		if (StringUtils.isBlank(id)) {
			setId(beanName);
		}
	}
	
	//=============
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setInterface(String interfaceName) {
		this.interfaceName = interfaceName;
		try {
			Class<?> clazz = Class.forName(interfaceName);
			setInterface(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Can't find class:" + interfaceName, e);
		}
	}
	
	public void setInterface(Class<?> interfaceClass) {
		Objects.requireNonNull(interfaceClass, "setInterface(Class<?> interfaceClass) param is null");
		if (!interfaceClass.isInterface()) {
			throw new IllegalArgumentException(interfaceClass.getName() + " is not an interface type.");
		}
		this.interfaceClass = interfaceClass;
	}
	
	public String getInterface() {
		return interfaceName;
	}
	
	public void setRef(T ref) {
		Objects.requireNonNull(ref, "setRef() param is null");
		this.ref = ref;
	}
	
	public T getRef() {
		return ref;
	}

	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	@Override
	public String toString() {
		return "CServiceBean{" +
				"applicationContext=" + applicationContext +
				", interfaceName='" + interfaceName + '\'' +
				", interfaceClass=" + interfaceClass +
				", ref=" + ref +
				", beanName='" + beanName + '\'' +
				", id='" + id + '\'' +
				", registry='" + registry + '\'' +
				'}';
	}
}

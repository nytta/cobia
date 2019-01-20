package lam.cobia.config.spring;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import lam.cobia.core.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(CRegistryBean.class);

	@ParamAnnotation
	private String id;

	@ParamAnnotation
	private String interfaceName;

	@ParamAnnotation
	private Class<?> interfaceClass;

	@ParamAnnotation
	private volatile T ref;

	@ParamAnnotation
	private String registry = "zookeeper"; //default registry:zookeeper

	@ParamAnnotation
	private String serviceServer; //This field must be initialzed, when value of field `registry` is 'direct'.
	
	private final AtomicBoolean refInited = new AtomicBoolean(false);
	
	private ApplicationContext applicationContext;
	
	//======================
	
	//The method of implementing interface org.springframework.beans.factory.InitializingBean
	@Override
	public void afterPropertiesSet() throws Exception {
		super.putParamIntoMap();
		LOGGER.info("[afterPropertiesSet] " + this.getClass().getSimpleName() + ", put param int map:" + super.getParams());
	}
	
	//The method of implementing interface org.springframework.beans.factory.DisposableBean
	@Override
	public void destroy() throws Exception {
		super.clearParams();
		LOGGER.info("[destroy] " + this.getClass().getSimpleName() + ", clear param map");
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
		LOGGER.info("[getObject] interface:" + interfaceClass + ", ref hashCode:" + System.identityHashCode(t));
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
			ref = ServiceFactory.takeDefaultInstance(Reference.class).refer((Class<T>)interfaceClass, this);
		}
		return ref;
	}

	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	public String getServiceServer() {
		return serviceServer;
	}

	public void setServiceServer(String serviceServer) {
		this.serviceServer = serviceServer;
	}
}

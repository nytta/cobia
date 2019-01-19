package lam.cobia.config.spring;

import java.util.Objects;

import lam.cobia.core.util.GsonUtil;
import lam.cobia.core.util.ParamConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(CServiceBean.class);
	
	public ApplicationContext applicationContext;

	@ParamAnnotation
	private String interfaceName;

	@ParamAnnotation
	private Class<?> interfaceClass;

	@ParamAnnotation
	private T ref;

	@ParamAnnotation
	private String beanName;

	@ParamAnnotation
	private String id;

	@ParamAnnotation
	private Integer weight;

	@ParamAnnotation
	private String registry = "zookeeper"; //default registry:zookeeper

	/**
	 * to determine whether balance service provider list.
	 */
	@ParamAnnotation
	private Boolean balanced = Boolean.FALSE;
	
	//=============
	
	//The method of implementing of interface org.springframework.beans.factory.InitializingBean
	@Override
	public void afterPropertiesSet() throws Exception {
		//初始化完bean的属性值之后
		putParamIntoMap();
		LOGGER.info("[afterPropertiesSet] " + this.getClass().getSimpleName() + ", put param int map:" + super.getParams());
	}
	
	//The method of implementing of interface org.springframework.beans.factory.DisposableBean
	@Override
	public void destroy() throws Exception {
		//销毁bean之前
		super.clearParams();
		LOGGER.info("[destroy] " + this.getClass().getSimpleName() + ", clear param map");
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
		LOGGER.info(event.getClass().getName());
		if (ContextRefreshedEvent.class.getName().equals(event.getClass().getName())) {
			//do export bean
			ServiceFactory.takeDefaultInstance(Service.class).export(getRef(), (Class<T>) interfaceClass, super.getParams());
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

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	public Boolean getBalanced() {
		return balanced;
	}

	public void setBalanced(Boolean balanced) {
		this.balanced = balanced;
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
				", weight=" + weight +
				", registry='" + registry + '\'' +
				", params=" + params +
				'}';
	}
}

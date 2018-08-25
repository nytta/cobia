package lam.cobia.config.spring;

import lam.cobia.config.spring.schema.RegistryBeanDefinitionParser;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.registry.RegistryConsumer;
import lam.cobia.registry.RegistryProvider;
import lam.cobia.spi.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @description: CRegistryBean
 * @author: linanmiao
 * @date: 2018/8/25 11:31
 * @version: 1.0
 */
public class CRegistryBean extends AbstractConfig
    implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener<ApplicationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRegistryBean.class);

    private static ApplicationContext applicationContext;

    private static RegistryConsumer registryConsumer;

    private static RegistryProvider registryProvider;

    @ParamAnnotation
    private String id;

    @ParamAnnotation
    private String type;

    @ParamAnnotation
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (!Type.has(type)) {
            throw new CobiaException("value[" + type + "] of type is illegal, please check it.");
        }
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void destroy() throws Exception {
        super.clearParams();
        LOGGER.info("[destroy] " + this.getClass().getSimpleName() + ", clear param map");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.putParamIntoMap();
        LOGGER.info("[afterPropertiesSet] " + this.getClass().getSimpleName() + ", put param int map:" + super.getParams());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CRegistryBean.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        LOGGER.info(applicationEvent.getClass().getName());
        if (ContextRefreshedEvent.class.getName().equals(applicationEvent.getClass().getName())) {
            CRegistryBean.registryConsumer = ServiceFactory.takeInstance(getType(), RegistryConsumer.class);
            CRegistryBean.registryProvider = ServiceFactory.takeInstance(getType(), RegistryProvider.class);
            CRegistryBean.registryConsumer.setParamMap(super.getParams());
            CRegistryBean.registryProvider.setParamMap(super.getParams()) ;
            LOGGER.info("[onApplicationEvent] RegistryConsumer:{}", registryConsumer.getClass().getName());
            LOGGER.info("[onApplicationEvent] RegistryProvider:{}", registryProvider.getClass().getName());
        }
    }

    public static RegistryProvider getRegistryProvider() {
        if (CRegistryBean.registryProvider == null) {
            throw new CobiaException("static field registryProvider is null in " + CRegistryBean.class.getName()
                                    + ", please check tag <" + RegistryBeanDefinitionParser.XML_NAME + " />");
        }
        return CRegistryBean.registryProvider;
    }

    public static RegistryConsumer getRegistryConsumer() {
        if (CRegistryBean.registryConsumer == null) {
            throw new CobiaException("static field registryConsumer is null in " + CRegistryBean.class.getName()
                                    + ", please check tag <" + RegistryBeanDefinitionParser.XML_NAME + " />");
        }
        return CRegistryBean.registryConsumer;
    }

    public static enum Type {

        DIRECT("direct"),
        ZOOKEEPER("zookeeper");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Type toMe(String value) {
            for (Type type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        public static boolean has(String value) {
            return toMe(value) != null;
        }
    }
}

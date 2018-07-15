package lam.cobia.config.spring.schema;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import lam.cobia.config.spring.CServiceBean;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年6月27日
* @version 1.0
*/
public class ServiceBeanDefinitionParser extends AbstractBeanDefinitionParser{
	
	public static final String XML_NAME = "service";
	
	public ServiceBeanDefinitionParser(boolean required) {
		super(XML_NAME, CServiceBean.class, required);
	}
	
	@Override
	protected String parseId(Element element, ParserContext parserContext, RootBeanDefinition beanDefinition) {
		String id = super.parseId(element, parserContext, beanDefinition);
		if (StringUtils.isBlank(id)) {
			id = element.getAttribute("interface");
		}
		return id;
	}

	@Override
	protected void parseCustom(Element element, ParserContext parserContext, RootBeanDefinition beanDefinition) {
		String className = element.getAttribute("class");
		if (StringUtils.isNotBlank(className)) {
			RootBeanDefinition classBeanDefinition = new RootBeanDefinition();
			try {
				classBeanDefinition.setBeanClass(Class.forName(className));
				classBeanDefinition.setLazyInit(false);
				
				parseProperties(element.getChildNodes(), beanDefinition);
				
				String id = (String) beanDefinition.getPropertyValues().get("id");
				beanDefinition.getPropertyValues().addPropertyValue("ref", new BeanDefinitionHolder(classBeanDefinition, id + "Impl"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new IllegalStateException("Can't find Class:" + className, e);
			}
		}		
	}

}

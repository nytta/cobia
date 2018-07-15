package lam.cobia.config.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年6月27日
* @version 1.0
*/
public class CobiaNamespaceHandler extends NamespaceHandlerSupport{

	@Override
	public void init() {
		registerBeanDefinitionParser(ServiceBeanDefinitionParser.XML_NAME, new ServiceBeanDefinitionParser(true));
		registerBeanDefinitionParser(ReferenceBeanDefinitionParser.XML_NAME, new ReferenceBeanDefinitionParser(true));
	}

}

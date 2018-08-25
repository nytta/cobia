package lam.cobia.config.spring.schema;

import lam.cobia.config.spring.CRegistryBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @description: RegistryBeanDefinitionParser
 * @author: linanmiao
 * @date: 2018/8/25 13:50
 * @version: 1.0
 */
public class RegistryBeanDefinitionParser extends AbstractBeanDefinitionParser{

    public static final String XML_NAME = "registry";

    public RegistryBeanDefinitionParser(boolean required) {
        super(XML_NAME, CRegistryBean.class, required);
    }

    @Override
    protected String parseId(Element element, ParserContext parserContext, RootBeanDefinition beanDefinition) {
        String id = super.parseId(element, parserContext, beanDefinition);
        if (StringUtils.isBlank(id)) {
            id = XML_NAME;
        }
        return id;
    }
}

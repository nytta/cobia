package lam.cobia.config.spring.schema;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年6月27日
* @version 1.0
*/
public abstract class AbstractBeanDefinitionParser implements BeanDefinitionParser{
	
	protected String xmlName;
	
	protected Class<?> clazz;
	
	protected boolean required;
	
	public AbstractBeanDefinitionParser(String xmlName, Class<?> clazz, boolean required) {
		this.xmlName = xmlName;
		this.clazz = clazz;
		this.required = required;
	}
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return parse(element, parserContext, clazz, required);
	}
	
	public BeanDefinition parse(Element element, ParserContext parserContext, Class<?> clazz, boolean required) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(clazz);
		beanDefinition.setLazyInit(false);
		String id = parseId(element, parserContext, beanDefinition);
		if (StringUtils.isBlank(id)) {
			throw new IllegalStateException("Has not id value on element(" + element.getTagName() + "):" + element.toString());			
		}
		if (parserContext.getRegistry().containsBeanDefinition(id)) {
			throw new IllegalStateException("Duplicate bean id " + id);
		}
		parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
		beanDefinition.getPropertyValues().addPropertyValue("id", id);
		
		parseCustom(element, parserContext, beanDefinition);
		
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			String attribute = null;
			int modifiers = method.getModifiers();
			if (method.getParameterTypes().length == 1 && 
				name.length() > 3 && name.startsWith("set") && 
				Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {				
				String attribute_ = name.substring(3);
				if (attribute_.length() > 1) {
					attribute = String.valueOf(attribute_.charAt(0)).toLowerCase() + attribute_.substring(1);
				} else {
					attribute = attribute_.toLowerCase();
				}
			}
			if (attribute == null) {
				continue ;
			}
			String attrValue = element.getAttribute(attribute);
			Class<?> parameterType = method.getParameterTypes()[0];
			if (isPrimitive(parameterType)) {
				beanDefinition.getPropertyValues().addPropertyValue(attribute, attrValue);
				continue ;
			}
			if ("ref".equals(attribute) && parserContext.getRegistry().containsBeanDefinition(attrValue)) {
				BeanDefinition refBeanDefinition = parserContext.getRegistry().getBeanDefinition(attrValue);
				if (!refBeanDefinition.isSingleton()) {
					throw new IllegalStateException("ref=" + attrValue + ", but bean " + attrValue + " is not singleton.");
				}
			}
			if (StringUtils.isNotBlank(attrValue)) {
				RuntimeBeanReference reference = new RuntimeBeanReference(attrValue);
				beanDefinition.getPropertyValues().addPropertyValue(attribute, reference);
			}
		}
		return beanDefinition;
		
	}
	
    public static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == Boolean.class || cls == Byte.class
                || cls == Character.class || cls == Short.class || cls == Integer.class
                || cls == Long.class || cls == Float.class || cls == Double.class
                || cls == String.class || cls == Date.class || cls == Class.class;
    }
	
	public static void parseProperties(NodeList nodeList, RootBeanDefinition rootBeanDefinition) {
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int index = 0; index < nodeList.getLength(); index++) {
				Node node = nodeList.item(index);
				if (node instanceof Element) {
					if ("property".equals(node.getNodeName()) || "property".equals(node.getLocalName())) {
						Element e = (Element) node;
						String value = e.getAttribute("value");
						if (value != null) {
							
						} else {
							String ref = e.getAttribute("ref");
							if (ref != null) {
								
							} else {
								throw new IllegalStateException("'property' neihter has value nor ref attribute.");
							}
						}
					}
				}
				
			}
		}
	}
	
	//SubClass can override method parseId to get id.
	protected String parseId(Element element, ParserContext parserContext, RootBeanDefinition beanDefinition) {
		return element.getAttribute("id");
	}
	
	//SubClass can override method parseCustom.
	protected void parseCustom(Element element, ParserContext parserContext, RootBeanDefinition beanDefinition) {
		
	}
	
	public String getXmlName() {
		return xmlName;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
}

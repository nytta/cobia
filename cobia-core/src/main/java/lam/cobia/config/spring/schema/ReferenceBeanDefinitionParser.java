package lam.cobia.config.spring.schema;

import lam.cobia.config.spring.CRefrenceBean;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年6月28日
* @version 1.0
*/
public class ReferenceBeanDefinitionParser extends AbstractBeanDefinitionParser{
	
	public static final String XML_NAME = "reference";

	public ReferenceBeanDefinitionParser(boolean required) {
		super(XML_NAME, CRefrenceBean.class, required);
	}

}

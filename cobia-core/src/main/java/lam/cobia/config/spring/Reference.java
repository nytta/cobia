package lam.cobia.config.spring;

import lam.cobia.spi.Spiable;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年7月8日
* @versio 1.0
*/
@Spiable("cobia")
public interface Reference {
	
	public <T> T refer(Class<T> clazz, CRefrenceBean<T> refrenceBean);

}

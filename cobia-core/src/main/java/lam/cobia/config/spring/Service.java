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
public interface Service {
	
	public <T> void export(T ref, Class<T> clazz);

}

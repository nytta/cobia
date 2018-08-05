package lam.cobia.serialize.support;

import lam.cobia.spi.Spiable;

/**
* <p>
* desrialize Object
* </p>
* @author linanmiao
* @date 2018年4月29日
* @versio 1.0
*/
@Spiable("hessian2")
public interface CobiaDeserializer {

	public <T> T deserialize(byte[] bytes, Class<T> clazz);
	
}

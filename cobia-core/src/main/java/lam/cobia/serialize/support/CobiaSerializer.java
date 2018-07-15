package lam.cobia.serialize.support;
/**
* <p>
* serialize Object
* </p>
* @author linanmiao
* @date 2018年4月29日
* @versio 1.0
*/
public interface CobiaSerializer {
	
	public byte[] serialize(Object resource, Class<?> clazz);

}

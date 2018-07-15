package lam.cobia.serialize.support;
/**
* <p>
* desrialize Object
* </p>
* @author linanmiao
* @date 2018年4月29日
* @versio 1.0
*/
public interface CobiaDeserializer {

	public <T> T deserialize(byte[] bytes, Class<T> clazz);
	
}

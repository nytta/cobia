package lam.cobia.serialize;

import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.remoting.planning.Request2;
import lam.cobia.remoting.planning.Response2;

/**
* <p>
* deserialize object with plugin protostuff.
* </p>
* @author linanmiao
* @date 2018年5月6日
* @versio 1.0
*/
public class ProtostuffDeserializer extends AbstractDeserializer{
	
	public ProtostuffDeserializer() {
		super("protostuff");
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		if (clazz.equals(Request2.class)) {
			Schema<Request2> schema = RuntimeSchema.getSchema(Request2.class);
			Request2 message = schema.newMessage();
			ProtostuffIOUtil.mergeFrom(bytes, message, schema);
			return (T) message;
		} else if (clazz.equals(Response2.class)){
			Schema<Response2> schema = RuntimeSchema.getSchema(Response2.class);
			Response2 message = schema.newMessage();
			ProtostuffIOUtil.mergeFrom(bytes, message, schema);
			return (T) message;
		}
		throw new CobiaException("deserializer not support class type:" + clazz.getName());
	}

}

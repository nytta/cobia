package lam.cobia.serialize;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.remoting.Request2;
import lam.cobia.remoting.Response2;

/**
* <p>
* serialize object with plugin protostuff.
* </p>
* @author linanmiao
* @date 2018年5月6日
* @versio 1.0
*/
public class ProtostuffSerializer extends AbstractSerializer{
	
	private int bufferSize = LinkedBuffer.DEFAULT_BUFFER_SIZE;
	
	public ProtostuffSerializer() {
		super("protostuff");
	}

	@Override
	public byte[] serialize(Object resource, Class<?> clazz) {
		if (!resource.getClass().equals(clazz)) {
			throw new CobiaException("Class type of resource(" + resource.getClass().getName() 
					+ ") is not equals to the Class:" + clazz.getName());
		}
		LinkedBuffer buffer = LinkedBuffer.allocate(bufferSize);
		byte[] bytes;
		if (resource instanceof Request2) {
			Schema<Request2> schema = RuntimeSchema.getSchema(Request2.class);
			try {
				bytes = ProtostuffIOUtil.toByteArray((Request2)resource, schema, buffer);
			} finally {
				buffer.clear();
			}
		} else if (resource instanceof Response2) {
			Schema<Response2> schema = RuntimeSchema.getSchema(Response2.class);
			try {
			bytes = ProtostuffIOUtil.toByteArray((Response2)resource, schema, buffer);
			} finally {
				buffer.clear();
			}
		} else {
			throw new CobiaException("not support resource type:" + resource.getClass().getName());
		}
		return bytes;
	}

}

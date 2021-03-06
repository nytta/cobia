package lam.cobia.serialize;

import com.google.protobuf.InvalidProtocolBufferException;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.log.Console;
import lam.cobia.remoting.planning.Request2;
import lam.cobia.remoting.planning.Request2Proto;
import lam.cobia.remoting.planning.Response2;
import lam.cobia.remoting.planning.Response2Proto;

/**
* <p>
* deserialize object with protobuf protocol.
* </p>
* @author linanmiao
* @date 2018年4月30日
* @versio 1.0
*/
public class ProtobufDeserializer extends AbstractDeserializer{
	
	public ProtobufDeserializer() {
		super("protobuf");
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		Console.println("class name:%s", clazz.getName());
		if (clazz == Request2.class) {
			try {
				Request2Proto.Request2 r2 = Request2Proto.Request2.parseFrom(bytes);
				//提供者方法参数对象的类型
				Class<?> dataClass = Class.forName(r2.getDataClassName());
				byte[] dataBytes = r2.getData().toByteArray();
				Object data = deserialize(dataBytes, dataClass);
				
				Request2 request2 = new Request2(r2.getId())
						.setInterfaceName(r2.getInterfaceName())
						.setMethod(r2.getMethod())
						.setParameterTypes(new Class[]{ Class.forName(r2.getDataClassName()) })
						.setArguments(new Object[] { r2.getData() });
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
				throw new CobiaException(e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new CobiaException(e);
			}
		} else if (clazz == Response2.class) {
			try {
				Response2Proto.Response2 r2 = Response2Proto.Response2.parseFrom(bytes);

				Class<?> dataClass = Class.forName(r2.getDataClassName());
				byte[] dataBytes = r2.getData().toByteArray();
				Object data = deserialize(dataBytes, dataClass);
				
				Response2 response2 = new Response2(r2.getId())
						.setDataClassName(r2.getDataClassName())
						.setData(data);
				return (T)response2;
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
				throw new CobiaException(e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new CobiaException(e);
			}
		}
		if (clazz == String.class) {
			String str = new String(bytes);
			return (T)str;
		}
		CobiaException e = new CobiaException("do not supported class:" + clazz.getName());
		e.printStackTrace();
		throw e;
	}

}

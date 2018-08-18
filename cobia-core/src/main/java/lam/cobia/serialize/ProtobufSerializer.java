package lam.cobia.serialize;

import com.google.protobuf.ByteString;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.log.Console;
import lam.cobia.remoting.planning.Request2;
import lam.cobia.remoting.planning.Request2Proto;
import lam.cobia.remoting.planning.Response2;
import lam.cobia.remoting.planning.Response2Proto;

/**
* <p>
* serialize object with protobuf protocol.
* </p>
* @author linanmiao
* @date 2018年4月30日
* @versio 1.0
*/
public class ProtobufSerializer extends AbstractSerializer{
	
	public ProtobufSerializer() {
		super("protobuf");
	}

	@Override
	public byte[] serialize(Object resource, Class<?> clazz) {
		Console.println("object class name:%s", clazz.getName());
		if (clazz == Request2.class) {
			Request2 request2 = (Request2) resource;
			Request2Proto.Request2 r2 = transferRequest2(request2);
			return r2.toByteArray();
		}
		if (clazz == Response2.class) {
			Response2 response2 = (Response2) resource;
			Response2Proto.Response2 r2 = transferResponse2(response2);
			return r2.toByteArray();
		}
		if (clazz == String.class) {
			String str = (String) resource;
			return str.getBytes();
		} else {
			CobiaException e = new CobiaException("do not supported class:" + clazz.getName());
			e.printStackTrace();
			throw e;
		}
	}
	
	private Request2Proto.Request2 transferRequest2(Request2 request2) {
		Byte[] byteObjs = (Byte[]) request2.getArguments();
		byte[] bytesCopy = new byte[byteObjs.length];
		for (int i = 0; i < byteObjs.length; i++) {
			bytesCopy[i] = byteObjs[i].byteValue();
		}
		ByteString bytes = ByteString.copyFrom(bytesCopy);
		Request2Proto.Request2.Builder builder = Request2Proto.Request2.newBuilder();
		builder.setId(request2.getId())
		.setInterfaceName(request2.getInterfaceName())
		.setMethod(request2.getMethod())
		.setDataClassName(request2.getArguments()[0].getClass().getName())
		.setData(bytes);
		return builder.build();
	}
	
	private Response2Proto.Response2 transferResponse2(Response2 response2) {
		ByteString bytes = ByteString.copyFrom((byte[]) response2.getData());
		Response2Proto.Response2.Builder builder = Response2Proto.Response2.newBuilder();
		builder.setId(response2.getId())
		.setDataClassName(response2.getDataClassName())
		.setData(bytes);
		return builder.build();
	}

}

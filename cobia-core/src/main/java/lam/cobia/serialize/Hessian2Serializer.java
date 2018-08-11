package lam.cobia.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Output;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.remoting.Request2;
import lam.cobia.remoting.Response2;

/**
* <p>
* hessian serializer
* </p>
* @author linanmiao
* @date 2018年1月3日
* @version 1.0
*/
public class Hessian2Serializer extends AbstractSerializer{
	
	public Hessian2Serializer() {
		super("hessian2");
	}
	
	@Override
	public byte[] serialize(Object resource, Class<?> clazz) {
		java.io.ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Hessian2Output hessian2Output = new Hessian2Output(outputStream);
		if (clazz == Request2.class) {

		} else if (clazz == Response2.class){

		}
		try {
			hessian2Output.writeObject(resource);
		} catch (IOException e) {
			throw new CobiaException(e);
		} finally {
			try {
				hessian2Output.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		try {
			outputStream.flush();
			byte[] bytes = outputStream.toByteArray();
			return bytes;
		} catch (IOException e) {
			throw new CobiaException(e);
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

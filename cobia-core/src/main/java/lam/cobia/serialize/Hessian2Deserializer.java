package lam.cobia.serialize;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.serialize.support.CobiaDeserializer;

/**
* <p>
* hessian deserializer
* </p>
* @author linanmiao
* @date 2018年1月3日
* @version 1.0
*/
public class Hessian2Deserializer extends AbstractSerializer implements CobiaDeserializer{
	
	public Hessian2Deserializer() {
		super("hessian2");
	}
	
	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		Hessian2Input hessian2Input = new Hessian2Input(inputStream);
		try {
			T t = (T) hessian2Input.readObject();
			return t;
		} catch (IOException e) {
			throw new CobiaException(e);
		} finally {
			try {
				hessian2Input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

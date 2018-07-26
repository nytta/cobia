package lam.cobia.config.spring;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月20日
* @versio 1.0
*/
public class AbstractConfig implements Serializable{

	private static final long serialVersionUID = -7216622573995725768L;

	protected Map<String, Object> params = new HashMap<String, Object>();

	protected void putParamIntoMap() {
		Field[] fields = getClass().getDeclaredFields();
		for (Field field: fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue ;
			}
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			ParamAnnotation paramAnno = field.getAnnotation(ParamAnnotation.class);
			if (paramAnno != null) {
				try {
					Object value = field.get(this);
					if (value != null) {
						params.put(field.getName(), value);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

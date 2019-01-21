package lam.cobia.rpc;

import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.ParameterUtil;
import lam.cobia.rpc.support.Consumer;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.rpc.support.Result;

import java.util.Map;
import java.util.Objects;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年7月7日
* @versio 1.0
*/
public abstract class AbstractConsumer<T> implements Consumer<T> {
	
	private Class<T> clazz;

	private Map<String, Object> params;

	private RegistryData registryData;
	
	public AbstractConsumer(Class<T> clazz, Map<String, Object> params, RegistryData registryData) {
		Objects.requireNonNull(clazz, "Class<T> clazz is null");
		Objects.requireNonNull(params, "Map<String, Object> params is null");
		Objects.requireNonNull(registryData, "param of RegistryData type is null");
		this.clazz = clazz;
		this.params = params;
		this.registryData = registryData;
	}
	
	@Override
	public String getKey() {
		return this.clazz.getName();
	}

	@Override
	public RegistryData getRegistryData() {
		return this.registryData;
	}

	@Override
	public Class<T> getInterface() {
		return this.clazz;
	}
	
	@Override
	public Result invoke(Invocation invocation) {
		return doInvoke(invocation);
	}

	@Override
	public void close() {
		
	}
	
	protected abstract Result doInvoke(Invocation invocation);

	@Override
	public void setParam(String key, Object value) {
		params.put(key, value);
	}

	@Override
	public String getParam(String key) {
		return ParameterUtil.getConfigParameter(key, params);
	}

	@Override
	public int getParamInt(String key) {
		String value = getParam(key);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			throw new IllegalArgumentException("value of key:" + key + " is " + value);
		}
	}

	@Override
	public boolean getParamBoolean(String key) {
		String value = getParam(key);
		try {
			return Boolean.parseBoolean(value);
		} catch (Exception e) {
			throw new IllegalArgumentException("value of key:" + key + " is " + value);
		}
	}

	@Override
	public String getParam(String key, String defaultValue) {
		String value = getParam(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	@Override
	public int getParamInt(String key, int defaultValue) {
		try {
			return getParamInt(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public boolean getParamBoolean(String key, boolean defaultValue) {
		try {
			return getParamBoolean(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}
}

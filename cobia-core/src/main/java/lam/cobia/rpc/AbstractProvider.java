package lam.cobia.rpc;

import lam.cobia.config.spring.CServiceBean;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.rpc.support.Provider;
import lam.cobia.rpc.support.Result;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年7月6日
* @versio 1.0
*/
public abstract class AbstractProvider<T> implements Provider<T> {
	
	private T proxy;
	
	private Class<T> clazz;

	private final CServiceBean<T> serviceBean;
	
	public AbstractProvider(T proxy, Class<T> clazz, CServiceBean<T> serviceBean) {
		this.proxy = proxy;
		this.clazz = clazz;
		this.serviceBean = serviceBean;
	}
	
	@Override
	public Result invoke(Invocation invocation) {
		String methodName = invocation.getMethod();
		Class<?>[] parameterTypes = invocation.getParameterTypes();
		Object[] arguments = invocation.getArguments();

		Object result = doInvoke(proxy, methodName, parameterTypes, arguments);

		return new DefaultResult().setValue(result);
	}
	
	protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments);
	
	@Override
	public String getKey() {
		return this.clazz.getName();
	}
	
	@Override
	public Class<T> getInterface() {
		return this.clazz;
	}

	@Override
	public CServiceBean<T> getServiceBean() {
		return serviceBean;
	}

	@Override
	public void close() {
	}

	@Override
	public String toString() {
		return "AbstractProvider{class: " + clazz.getName() + "}";
	}
}

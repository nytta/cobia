package lam.cobia.rpc;

import java.io.Serializable;
import java.util.Arrays;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.rpc.support.Invocation;

/**
* <p>
* default invocation
* </p>
* @author linanmiao
* @date 2017年12月19日
* @version 1.0
*/
public class DefaultInvocation implements Invocation, Serializable{
	
	private static final long serialVersionUID = 7937071307339783242L;
	
	private String interfacename;

	private String method;
	
	private Class<?>[] paramenterTypes;
	
	private Object[] arguments;
	
	public DefaultInvocation setInterface(Class<?> clazz) {
		Class<?>[] classes = clazz.getInterfaces();
		if (classes == null) {
			throw new CobiaException("interface of " + clazz.getName() + " is null");
		} else if (classes.length == 0){
			this.interfacename = clazz.getName();
		} else {
			this.interfacename = classes[0].getName();
		}
		return this;
	}
	
	public DefaultInvocation setInterface(String interfacename) {
		this.interfacename = interfacename;
		return this;
	}
	
	public DefaultInvocation setMethod(String method) {
		this.method = method;
		return this;
	}
	
	public DefaultInvocation setParamenterTypes(Class<?>[] paramenterTypes) {
		this.paramenterTypes = paramenterTypes;
		return this;
	}
	
	public DefaultInvocation setArguments(Object[] arguments) {
		this.arguments = arguments;
		return this;
	}
	
	@Override
	public String getInterface() {
		return interfacename;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return paramenterTypes;
	}

	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public String toString() {
		return "DefaultInvocation [method=" + method + ", paramenterTypes=" + Arrays.toString(paramenterTypes)
				+ ", arguments=" + Arrays.toString(arguments) + "]";
	}
	
}

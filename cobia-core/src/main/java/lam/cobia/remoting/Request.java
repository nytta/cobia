package lam.cobia.remoting;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import lam.cobia.core.util.GsonUtil;

/**
* <p>
* request model
* </p>
* @author linanmiao
* @date 2017年12月22日
* @version 1.0
*/
public class Request implements IRequest, Serializable{
	
	private static final long serialVersionUID = 7473629229906171995L;

	private final long id;

	private String interfaceName;

	private String method;

	private Class<?>[] parameterTypes;

	private Object[] arguments;
	
	private static final AtomicLong INVOKER_ID = new AtomicLong();
	
	public Request() {
		this.id = newId();
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return this.parameterTypes;
	}

	@Override
	public Object[] getArguments() {
		return this.arguments;
	}

	@Override
	public String getInterfaceName() {
		return this.interfaceName;
	}
	
	@Override
	public String getMethod() {
		return this.method;
	}

	public Request setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		return this;
	}

	public Request setMethod(String method) {
		this.method = method;
		return this;
	}

	public Request setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
		return this;
	}

	public Request setArguments(Object[] arguments) {
		this.arguments = arguments;
		return this;
	}

	public static Request newRequest() {
		return new Request();
	}
	
	private static long newId() {
		return INVOKER_ID.incrementAndGet();
	}

}

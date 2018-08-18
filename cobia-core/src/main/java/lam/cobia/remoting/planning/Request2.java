package lam.cobia.remoting.planning;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import lam.cobia.remoting.IRequest;

/**
* <p>
* request model
* </p>
* @author linanmiao
* @date 2017年12月22日
* @version 1.0
*/
public class Request2 implements IRequest, Serializable{
	
	private static final long serialVersionUID = 7473629229906171995L;

	private final long id;
	
	private String interfaceName;
	
	public String method;

	private Class<?>[] parameterTypes;

	private Object[] arguments;
	
	public Class<?>[] getParameterTypes() {
		return this.parameterTypes;
	}
	
	public Object[] getArguments() {
		return this.arguments;
	}

	public Request2 setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
		return this;
	}

	public Request2 setArguments(Object[] arguments) {
		this.arguments = arguments;
		return this;
	}

	private static final AtomicLong INVOKER_ID = new AtomicLong();
	
	public Request2() {
		this(newId());
	}
	
	public Request2(long id) {
		this.id = id;
	}
	
	public String getInterfaceName() {
		return interfaceName;
	}

	public Request2 setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		return this;
	}

	public String getMethod() {
		return method;
	}

	public Request2 setMethod(String method) {
		this.method = method;
		return this;
	}


	public long getId() {
		return id;
	}

	public static Request2 newRequest2() {
		return new Request2();
	}
	
	public static Request2 newReques2(long id) {
		return new Request2(id);
	}
	
	private static long newId() {
		return INVOKER_ID.incrementAndGet();
	}
	
}

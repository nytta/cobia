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
public class Request2 implements IRequest, Serializable{
	
	private static final long serialVersionUID = 7473629229906171995L;

	private final long id;
	
	private String interfaceName;
	
	public String method;
	
	//暂不支持方法重载
	//public Class<?>[] getParameterTypes();
	
	//public Object[] getArguments();
	
	//对应参数的类名
	private String dataClassName;

	//对应参数的对象
	private Object data;
	
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

	public String getDataClassName() {
		return dataClassName;
	}

	public Request2 setDataClassName(String dataClassName) {
		this.dataClassName = dataClassName;
		return this;
	}

	public Object getData() {
		return data;
	}

	public Request2 setData(Object data) {
		this.data = data;
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

	@Override
	public String toString() {
		return "Request2 [id=" + id + ", interfaceName=" + interfaceName + ", method=" + method + ", dataClassName="
				+ dataClassName + ", data=" + data + "]";
	}
	
}

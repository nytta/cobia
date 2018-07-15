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
	
	private String dataClassName;

	private Object data;
	
	private static final AtomicLong INVOKER_ID = new AtomicLong();
	
	public Request() {
		this.id = newId();
	}
	
	public IBody setDataClassName(String dataClassName) {
		this.dataClassName = dataClassName;
		return this;
	}
	
	public IBody setData(Object data) {
		this.data = data;
		return this;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public String getInterfaceName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getDataClassName() {
		return dataClassName;
	}
	
	@Override
	public Object getData() {
		return data;
	}
	
	public static Request newRequest() {
		return new Request();
	}
	
	private static long newId() {
		return INVOKER_ID.incrementAndGet();
	}

	@Override
	public String toString() {
		return "Request [id=" + id + ", dataClassName=" + dataClassName + ", data=" + data + "]";
	}

}

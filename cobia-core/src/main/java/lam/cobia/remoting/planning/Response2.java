package lam.cobia.remoting.planning;

import lam.cobia.remoting.IResponse;

import java.io.Serializable;

/**
* <p>
* response for rpc
* </p>
* @author linanmiao
* @date 2017年12月29日
* @version 1.0
*/
public class Response2 implements IResponse, Serializable{

	private static final long serialVersionUID = -5687604797173954790L;
	
	private long id;
	
	private String dataClassName;
	
	private Object data;
	
	public Response2(long id) {
		this.id = id;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	public Response2 setDataClassName(String dataClassName) {
		this.dataClassName = dataClassName;
		return this;
	}
	
	@Override
	public String getDataClassName() {
		return dataClassName;
	}
	
	public Response2 setData(Object data) {
		this.data = data;
		return this;
	}
	
	@Override
	public Object getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Response [id=" + id + ", dataClassName=" + dataClassName + ", data=" + data + "]";
	}

}

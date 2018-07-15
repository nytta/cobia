package lam.cobia.remoting;

import java.io.Serializable;

/**
* <p>
* response for rpc
* </p>
* @author linanmiao
* @date 2017年12月29日
* @version 1.0
*/
public class Response implements IResponse, Serializable{

	private static final long serialVersionUID = -5687604797173954790L;
	
	private long id;
	
	private String dataClassName;
	
	private Object data;
	
	public Response(long id) {
		this.id = id;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	public Response setDataClassName(String dataClassName) {
		this.dataClassName = dataClassName;
		return this;
	}
	
	@Override
	public String getDataClassName() {
		return dataClassName;
	}
	
	public Response setData(Object data) {
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

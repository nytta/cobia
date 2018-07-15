package lam.cobia.remoting;
/**
* <p>
* interface body
* </p>
* @author linanmiao
* @date 2018年5月5日
* @versio 1.0
*/
public interface IBody {
	
	public long getId();
	
	public String getDataClassName();
	
	public IBody setDataClassName(String dataClassName);
	
	public Object getData();

	public IBody setData(Object data);
}

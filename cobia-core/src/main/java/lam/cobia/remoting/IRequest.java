package lam.cobia.remoting;
/**
* <p>
* entity for request(client) send to server.
* </p>
* @author linanmiao
* @date 2018年5月1日
* @versio 1.0
*/
public interface IRequest extends IBody{
	
	//public long getId();
	
	public String getInterfaceName();
	
	public String getMethod();
	
	//public String getDataClassName();

	//public Object getData();

}

package lam.cobia.remoting;
/**
* <p>
* entity for request(client) send to server. <br/>
 * IRequest: { <br/>
 * interfaceName: string, <br/>
 * method: string <br/>
 * } <br/>
* </p>
* @author linanmiao
* @date 2018年5月1日
* @versio 1.0
*/
public interface IRequest extends IBody{
	
	public String getInterfaceName();
	
	public String getMethod();

}

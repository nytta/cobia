package lam.cobia.remoting;
/**
* <p>
* entity for request(client) send to server. <br/>
 * IRequest: { <br/>
 * id: long, <br/>
 * interfaceName: string, <br/>
 * method: string, <br/>
 * parameterTypes: [], <br/>
 * arguments: [] <br/>
 * } <br/>
* </p>
* @author linanmiao
* @date 2018年5月1日
* @versio 1.0
*/
public interface IRequest {
	
	public String getInterfaceName();
	
	public String getMethod();

	public long getId();

	public Class<?>[] getParameterTypes();

	public Object[] getArguments();

}

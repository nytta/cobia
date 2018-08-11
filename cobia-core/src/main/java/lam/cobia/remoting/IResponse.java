package lam.cobia.remoting;

/**
* <p>
* entity for server response to client. <br/>
 * IResponse:  <br/>
 *{ <br/>
 * id: long, <br/>
 * dataClassName: string, <br/>
 * data: object <br/>
 * } <br/>
* </p>
* @author linanmiao
* @date 2017年12月29日
* @version 1.0
*/
public interface IResponse{

    public long getId();

    public String getDataClassName();

    public Object getData();

}

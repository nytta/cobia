package lam.cobia.remoting;

/**
* <p>
* response future
* </p>
* @author linanmiao
* @date 2017年12月22日
* @version 1.0
*/
public interface ResponseFuture {
	
	public Object get();
	
	public Object get(long milliseconds);

}

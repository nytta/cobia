package lam.cobia.rpc;
/**
* <p>
* result of rpc
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public interface Result {
	
	public Object getValue();
	
	public Exception getException();
	
	public boolean hasException();

}

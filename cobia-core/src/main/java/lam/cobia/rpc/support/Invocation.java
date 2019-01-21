package lam.cobia.rpc.support;
/**
* <p>
* invocation
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public interface Invocation {
	
	public String getInterface();
	
	public String getMethod();
	
	public Class<?>[] getParameterTypes();
	
	public Object[] getArguments();

}

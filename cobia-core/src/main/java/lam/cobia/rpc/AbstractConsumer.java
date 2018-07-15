package lam.cobia.rpc;

import java.util.Objects;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年7月7日
* @versio 1.0
*/
public abstract class AbstractConsumer<T> implements Consumer<T>{
	
	private Class<T> clazz;
	
	public AbstractConsumer(Class<T> clazz) {
		Objects.requireNonNull(clazz, "Class<T> clazz is null");
		this.clazz = clazz;
	}
	
	@Override
	public String getKey() {
		return this.clazz.getName();
	}
	
	@Override
	public Class<T> getInterface() {
		return this.clazz;
	}
	
	@Override
	public Result invoke(Invocation invocation) {
		return doInvoke(invocation);
	}
	
	@Override
	public void close() {
		
	}
	
	protected abstract Result doInvoke(Invocation invocation);

}

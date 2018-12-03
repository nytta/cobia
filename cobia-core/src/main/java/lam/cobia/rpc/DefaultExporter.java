package lam.cobia.rpc;

import lam.cobia.rpc.support.Exporter;
import lam.cobia.rpc.support.Provider;

/**
* <p>
* default exporter
* </p>
* @author linanmiao
* @date 2018年1月2日
* @version 1.0
*/
public class DefaultExporter<T> implements Exporter<T> {

	private final Provider<T> provider;
	
	private volatile boolean closed = false;
	
	public DefaultExporter(Provider<T> provider) {
		this.provider = provider;
	}
	
	@Override
	public String getKey() {
		return getProvider().getKey();
	}
	
	@Override
	public Provider<T> getProvider() {
		return this.provider;
	}

	@Override
	public void close() {
		if (!closed) {
			closed = true;
			getProvider().close();
		}
	}

}

package lam.cobia.rpc;

import java.io.Closeable;

import lam.cobia.spi.Spiable;

/**
* <p>
* protocol
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
@Spiable("default")
public interface Protocol extends Closeable{
	
	public <T> Exporter<T> export(Provider<T> provider);
	
	public <T> Consumer<T> refer(Class<T> clazz);
	
	public void close();
}

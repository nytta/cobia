package lam.cobia.rpc;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.core.util.NetUtil;
import lam.cobia.remoting.*;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年7月7日
* @versio 1.0
*/
public class DefaultConsumer<T> extends AbstractConsumer<T>{
	
	private Client client;

	public DefaultConsumer(Class<T> clazz, Map<String, Object> params, Client client, RegistryData registryData) {
		super(clazz, params, registryData);
		this.client = client;
	}

	@Override
	protected Result doInvoke(Invocation invocation) {
		if (client.isClose()) {
			throw new CobiaException("client(to server " + NetUtil.parseToString(client.getServerAddress()) + ") has been closed.");
		}
		Channel channel = client.getChannel();

		Request request = Request.newRequest()
		.setInterfaceName(invocation.getInterface())
		.setMethod(invocation.getMethod());
		if (invocation.getParameterTypes() == null) {
			request.setParameterTypes(new Class<?>[]{});
		} else {
			request.setParameterTypes(invocation.getParameterTypes());
		}
		if (invocation.getArguments() == null) {
			request.setArguments(new Object[]{});
		} else {
			request.setArguments(invocation.getArguments());
		}
		DefaultFuture future = new DefaultFuture(request, channel);

		channel.send(request);

		Object response = future.get();
		if (response == null) {
			throw new CobiaException("response is null");
		}
		if (!(response instanceof Response)) {
			throw new CobiaException("should be type:" + Response.class.getName() + ", but get type:" + response.getClass().getName());
		}
		DefaultResult result = new DefaultResult();
		result.setValue(((Response) response).getData());
		return result;
	}
	
	@Override
	public void close() {
		if (client != null) {
			client.close();
		}
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this);
	}
}

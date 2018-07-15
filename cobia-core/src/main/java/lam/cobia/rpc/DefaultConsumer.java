package lam.cobia.rpc;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.core.util.NetUtil;
import lam.cobia.remoting.Channel;
import lam.cobia.remoting.Client;
import lam.cobia.remoting.DefaultFuture;
import lam.cobia.remoting.Request2;
import lam.cobia.remoting.Response;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年7月7日
* @versio 1.0
*/
public class DefaultConsumer<T> extends AbstractConsumer<T>{
	
	private Client[] clients;

	public DefaultConsumer(Class<T> clazz, Client[] clients) {
		super(clazz);
		this.clients = clients;
	}

	@Override
	protected Result doInvoke(Invocation invocation) {
		//select one of clients to invoke the invocation
		Client client = clients[0];
		if (client.isClose()) {
			throw new CobiaException("client(to server " + NetUtil.parseToString(client.getServerAddress()) + ") has been closed.");
		}
		Channel channel = client.getChannel();

		Request2 request = Request2.newRequest2()
		.setInterfaceName(super.getInterface().getName())
		.setMethod(invocation.getMethod());
		if (invocation.getParameterTypes() == null || invocation.getParameterTypes().length == 0) {
			
		} else {
			request
			.setDataClassName(invocation.getParameterTypes()[0].getName())
			.setData(invocation.getArguments()[0]);
		}
		DefaultFuture future = new DefaultFuture(request, channel);
		channel.send(request);
		Object obj = future.get();
		if (obj == null) {
			throw new CobiaException("result is null");
		}
		DefaultResult result = new DefaultResult();
		if (obj instanceof Response) {
			Response response = (Response) obj;
			return result.setValue(response.getData());
		} else {
			return result.setValue(obj);
		}
	}
	
	@Override
	public void close() {
		for (Client client : clients) {
			client.close();
		}
	}

}

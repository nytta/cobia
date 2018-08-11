package lam.cobia.remoting;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.log.Console;
import lam.cobia.rpc.DefaultInvocation;
import lam.cobia.rpc.Exporter;
import lam.cobia.rpc.Result;

/**
* <p>
* default channel handler
* </p>
* @author linanmiao
* @date 2018年1月2日
* @version 1.0
*/
public class DefaultChannelHanlder implements ChannelHandler{

	//private Invoker<?> invoker;
	private ConcurrentMap<String, Exporter<?>> exporterMap;

	public DefaultChannelHanlder(ConcurrentMap<String, Exporter<?>> exporterMap) {
		Objects.requireNonNull(exporterMap, "exporterMap is null");
		this.exporterMap = exporterMap;
	}
	
	@Override
	public void received(Channel channel, Object msg) {
		Console.println(channel + ">>>" + msg);
		if (msg instanceof Request) {
			Request request = (Request) msg;

			//invoker应该由Invocation的key来决定。
			Exporter<?> exporter = exporterMap.get(request.getInterfaceName());
			if (exporter == null) {
				throw new IllegalStateException("Exporter of " + request.getInterfaceName() + " is null");
			}

			DefaultInvocation invocation = new DefaultInvocation()
					.setInterface(request.getInterfaceName())
					.setMethod(request.getMethod())
					.setParamenterTypes(request.getParameterTypes())
					.setArguments(request.getArguments());

			Result result = exporter.getProvider().invoke(invocation);

			if (!result.hasException()) {
				Object resultValue = result.getValue();
				Response response = new Response(request.getId())
						.setDataClassName(resultValue.getClass().getName())
						.setData(resultValue);

				//回复客户端
				channel.send(response);
			} else {
				throw new CobiaException("Occurs exception", result.getException());
			}
			
		} else {
			throw new IllegalStateException("Server has received request, but do not support class type " + msg.getClass().getName());
		}
		
	}

}

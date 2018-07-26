package lam.cobia.remoting;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

import lam.cobia.core.exception.CobiaException;
import lam.cobia.log.Console;
import lam.cobia.rpc.DefaultInvocation;
import lam.cobia.rpc.Exporter;
import lam.cobia.rpc.Invocation;
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
	
	/*public DefaultChannelHanlder(Invoker<?> invoker) {
		this.invoker = invoker;
	}*/
	public DefaultChannelHanlder(ConcurrentMap<String, Exporter<?>> exporterMap) {
		Objects.requireNonNull(exporterMap, "exporterMap is null");
		this.exporterMap = exporterMap;
	}
	
	@Override
	public void received(Channel channel, Object msg) {
		Console.println(channel + ">>>" + msg);
		if (msg instanceof Request) {
			Request request = (Request) msg;
			
			if (request.getData() instanceof Invocation) {
				Invocation invocation = (Invocation) request.getData();
				//invoker应该由Invocation的key来决定。
				Exporter<?> exporter = exporterMap.get(invocation.getInterface());
				Objects.requireNonNull(exporter, "Exporter of key:[" + invocation.getInterface() + "] is null");
				Result result = exporter.getProvider().invoke(invocation);
				
				if (!result.hasException()) {
					Object resultValue = result.getValue();
					Response response = new Response(request.getId())
							.setDataClassName(resultValue.getClass().getName())
							.setData(resultValue);
					
					channel.send(response);
				} else {
					throw new CobiaException("Occurs exception", result.getException());
				}
			} else {
				throw new CobiaException("Do not support type:" + request.getData().getClass().getName());
			}
			
		} else if (msg instanceof Request2){
			//serializer data with protobuf
			Request2 request2 = (lam.cobia.remoting.Request2) msg;
			DefaultInvocation invocation = null;
			try {
				Class<?> interfac = Class.forName(request2.getInterfaceName());
				Class<?> paramenterType = Class.forName(request2.getDataClassName());
				invocation = new DefaultInvocation()
						.setInterface(interfac)
						.setMethod(request2.getMethod())
						.setParamenterTypes(new Class<?>[]{paramenterType})
						.setArguments(new Object[]{request2.getData()});
			} catch (CobiaException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			//invoker应该由Invocation的key来决定。
			Exporter<?> exporter = exporterMap.get(invocation.getInterface());
			Objects.requireNonNull(exporter, "Exporter of [" + invocation.getInterface() + "] is null");
			
			Result result = exporter.getProvider().invoke(invocation);
			     
			if (!result.hasException()) {
				Object resultValue = result.getValue();
				Response2 response = new Response2(request2.getId())
						.setDataClassName(resultValue.getClass().getName())
						.setData(resultValue);
				
				channel.send(response);
			} else {
				throw new CobiaException("Occurs exception", result.getException());
			}
		}
		
	}

}

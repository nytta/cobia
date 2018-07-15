package lam.cobia.log.filter;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月17日
* @version 1.0
*/
public class LamAsyncFilter extends Filter{

	/**
	 * @return Filter.DENY or Filter.ACCEPT or Filter.NEUTRAL 
	 */
	@Override
	public int decide(LoggingEvent event) {
		/*String message = (String) event.getMessage();
		if(message == null || !event.getLoggerName().equals("LogTest"))
			return Filter.ACCEPT;
		String[] ms = message.split(" ");
		if(ms.length == 2){
			try{
				int n = Integer.parseInt(ms[1]);
				if(n % 2 == 0)
					return Filter.ACCEPT;
				else
					return Filter.DENY;
			}catch(NumberFormatException e){
				return Filter.DENY;
			}
		}
		return Filter.DENY;*/
		return Filter.ACCEPT;
	}

}

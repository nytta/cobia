package lam.cobia.core.util;

import java.io.Closeable;
import java.io.Flushable;

/**
* <p>
* io util
* </p>
* @author linanmiao
* @date 2017年3月22日
* @version 1.0
*/
public class FinalizeUtils {
	
	/**
	 * close closeable instance quietly ignore exception.
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable){
		close(closeable, true);
	}
	
	public static void closeQuietly(Closeable ...closeables){
		for(Closeable closeable : closeables){
			closeQuietly(closeable);
		}
	}
	
	public static void closeNotQuietly(Closeable closeable){
		close(closeable, false);
	}
	
	private static void close(Closeable closeable, boolean ignoreException){
		if(closeable != null){
			try{
				if(closeable instanceof Flushable){
					((Flushable) closeable).flush();
				}
				closeable.close();
			}catch(Exception e){
				if(!ignoreException){
					e.printStackTrace();
				}
			}
		}
	}

}

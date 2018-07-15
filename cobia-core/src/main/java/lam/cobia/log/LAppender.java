package lam.cobia.log;

import java.io.Closeable;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月21日
* @version 1.0
*/
public interface LAppender extends Closeable{
	
	public LAppender append(String log);
	
}

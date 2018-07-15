package lam.cobia.rpc;

import java.io.Serializable;
import java.util.Objects;

/**
* <p>
* default result
* </p>
* @author linanmiao
* @date 2017年12月22日
* @version 1.0
*/
public class DefaultResult implements Result, Serializable{
	
	private static final long serialVersionUID = 3397816613697512112L;

	private Object value;
	
	private Exception exception;
	
	private boolean isException;
	
	public DefaultResult setValue(Object value) {
		this.value = value;
		return this;
	}
	
	public DefaultResult setException(Exception exception) {
		this.exception = Objects.requireNonNull(exception, "Exception object can't be null.");
		this.isException = true;
		return this;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Exception getException() {
		return exception;
	}

	@Override
	public boolean hasException() {
		return isException;
	}

	@Override
	public String toString() {
		return "DefaultResult [value=" + value + ", exception=" + exception + ", isException=" + isException + "]";
	}

}

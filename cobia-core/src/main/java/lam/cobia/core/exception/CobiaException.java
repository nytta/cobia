package lam.cobia.core.exception;
/**
* <p>
* exception class
* </p>
* @author linanmiao
* @date 2017年12月18日
* @version 1.0
*/
public class CobiaException extends RuntimeException {

	private static final long serialVersionUID = -5209665104999722655L;
	
    public CobiaException() {
        super();
    }

    public CobiaException(String message) {
        super(message);
    }

    public CobiaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CobiaException(Throwable cause) {
        super(cause);
    }

}

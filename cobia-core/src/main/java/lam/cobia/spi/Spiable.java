package lam.cobia.spi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* <p>
* cobia
* </p>
* @author linanmiao
* @date 2018年7月8日
* @versio 1.0
*/
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Spiable {
	
	String value() default "";

}

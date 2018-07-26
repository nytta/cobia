package lam.cobia.config.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: ParamAnnotation
 * @author: linanmiao
 * @date: 2018/7/26 9:57
 * @version: 1.0
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamAnnotation {

}

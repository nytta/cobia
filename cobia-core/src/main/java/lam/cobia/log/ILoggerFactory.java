package lam.cobia.log;

/**
 * slf4j ~ version 1.7.21
 * @author nytta
 * @date 2019/3/6
 */
public interface ILoggerFactory extends org.slf4j.ILoggerFactory{

    Logger getCobiaLogger(String name);
}

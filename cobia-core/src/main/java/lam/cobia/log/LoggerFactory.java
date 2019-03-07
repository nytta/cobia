package lam.cobia.log;

/**
 * slf4j ~ version 1.7.21
 * @author nytta
 * @date 2019/3/6
 */
public class LoggerFactory {

    private org.slf4j.ILoggerFactory iLoggerFactory;

    public void setILoggerFactory(final org.slf4j.ILoggerFactory iLoggerFactory) {
        this.iLoggerFactory = iLoggerFactory;
    }

    public static Logger getLogger(Class<?> clazz) {
        return new CobiaLogger(org.slf4j.LoggerFactory.getLogger(clazz));
    }

    public static Logger getLogger(String name) {
        return new CobiaLogger(org.slf4j.LoggerFactory.getLogger(name));
    }

    public static org.slf4j.ILoggerFactory getILoggerFactory() {
        return org.slf4j.LoggerFactory.getILoggerFactory();
    }

}

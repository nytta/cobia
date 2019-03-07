package lam.cobia.log;

/**
 * slf4j ~ version 1.7.21
 *
 * @author nytta
 * @date 2019/3/7
 */
public class CobiaLoggerFactory implements ILoggerFactory{

    private final org.slf4j.ILoggerFactory iLoggerFactory;

    public CobiaLoggerFactory(final org.slf4j.ILoggerFactory iLoggerFactory) {
        this.iLoggerFactory = iLoggerFactory;
    }

    @Override
    public org.slf4j.Logger getLogger(final String name) {
        return iLoggerFactory.getLogger(name);
    }

    @Override
    public Logger getCobiaLogger(final String name) {
        org.slf4j.Logger logger = iLoggerFactory.getLogger(name);
        return new CobiaLogger(logger);
    }
}

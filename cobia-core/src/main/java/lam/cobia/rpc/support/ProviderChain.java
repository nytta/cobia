package lam.cobia.rpc.support;

/**
 * @author: linanmiao
 */
public interface ProviderChain {

    public <T> Result invoke(Provider<T> next, Invocation invocation);

}

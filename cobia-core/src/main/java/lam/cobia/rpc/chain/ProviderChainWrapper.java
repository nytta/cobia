package lam.cobia.rpc.chain;

import java.util.List;
import java.util.Objects;

import lam.cobia.config.spring.CServiceBean;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.rpc.support.Provider;
import lam.cobia.rpc.support.ProviderChain;
import lam.cobia.rpc.support.Result;

/**
 * @author: linanmiao
 */
public class ProviderChainWrapper<T> implements Provider<T> {

    protected final Provider<T> provider;

    protected final ProviderChain next;

    public ProviderChainWrapper(Provider<T> provider, ProviderChain next) {
        Objects.requireNonNull(provider, "arguments provider is null");
        this.provider = provider;
        this.next     = next;
    }

    @Override
    public String getKey() {
        return this.provider.getKey();
    }

    @Override
    public Class<T> getInterface() {
        return this.provider.getInterface();
    }

    @Override
    public CServiceBean<T> getServiceBean() {
        return this.provider.getServiceBean();
    }

    @Override
    public Result invoke(Invocation invocation) {
        if (hasNext()) {
            return this.next().invoke(provider, invocation);
        }
        return this.provider.invoke(invocation);
    }

    public boolean hasNext() {
        return this.next != null;
    }

    public ProviderChain next() {
        return this.next;
    }

    @Override
    public void close() {
        this.provider.close();
    }
}

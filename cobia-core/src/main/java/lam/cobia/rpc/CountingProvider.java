package lam.cobia.rpc;

import java.util.List;

import lam.cobia.core.NotNegativeLong;
import lam.cobia.core.exception.CobiaException;
import lam.cobia.rpc.chain.ProviderChainWrapper;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.rpc.support.Provider;
import lam.cobia.rpc.support.ProviderChain;
import lam.cobia.rpc.support.Result;

/**
 * @author: linanmiao
 */
public class CountingProvider<T> extends ProviderChainWrapper {

    final NotNegativeLong invokedCount = new NotNegativeLong(0);

    public CountingProvider(Provider<T> provider) {
        super(provider, null);
    }

    public CountingProvider(Provider<T> provider, ProviderChain next) {
        super(provider, next);
    }

    @Override
    public Result invoke(Invocation invocation) {
        invokedCount.incrementAndGet();
        try {
            return super.invoke(invocation);
        } finally {
            invokedCount.decrementAndGet();
        }
    }

}

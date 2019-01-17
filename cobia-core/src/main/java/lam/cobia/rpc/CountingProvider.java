package lam.cobia.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lam.cobia.core.NotNegativeLong;
import lam.cobia.core.util.concurrent.ThreadFactoryBuilder;
import lam.cobia.rpc.chain.ProviderChainWrapper;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.rpc.support.Provider;
import lam.cobia.rpc.support.ProviderChain;
import lam.cobia.rpc.support.Result;

/**
 * @author: linanmiao
 */
public class CountingProvider<T> extends ProviderChainWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountingProvider.class);

    private static final long ONE_MINUTE_SECOND = 60;

    private static final int CORE_POOL_SIZE     = 1;

    private static final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
            CORE_POOL_SIZE,
            new ThreadFactoryBuilder().setThreadNamePrefix("CountingProvider").build(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    final NotNegativeLong invokedCount = new NotNegativeLong(0);

    public CountingProvider(Provider<T> provider) {
        this(provider, null);
    }

    public CountingProvider(Provider<T> provider, ProviderChain next) {
        super(provider, next);
        start();
    }

    private void start() {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                LOGGER.debug("collect provider:{} invoking count:{}", getKey(), invokedCount.get());
            },
            ONE_MINUTE_SECOND,
            ONE_MINUTE_SECOND,
            TimeUnit.SECONDS);
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

    @Override
    public void close() {
        super.close();
        scheduledThreadPoolExecutor.shutdown();
    }
}

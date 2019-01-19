package lam.cobia.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lam.cobia.config.spring.CRegistryBean;
import lam.cobia.core.NotNegativeLong;
import lam.cobia.core.model.RegistryData;
import lam.cobia.core.util.GsonUtil;
import lam.cobia.core.util.concurrent.ThreadFactoryBuilder;
import lam.cobia.registry.RegistryProvider;
import lam.cobia.rpc.chain.ProviderChainWrapper;
import lam.cobia.rpc.support.Invocation;
import lam.cobia.rpc.support.Provider;
import lam.cobia.rpc.support.ProviderChain;
import lam.cobia.rpc.support.Result;
import lam.cobia.spi.ServiceFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author: linanmiao
 */
public class BalancedProvider<T> extends ProviderChainWrapper implements DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalancedProvider.class);

    private static final long ONE_MINUTE_SECOND = 60;

    private static final int CORE_POOL_SIZE     = 1;

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
            CORE_POOL_SIZE,
            new ThreadFactoryBuilder().setThreadNamePrefix("BalancedProvider").build(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    final NotNegativeLong invokedCount = new NotNegativeLong(0);

    public BalancedProvider(Provider<T> provider) {
        this(provider, null);
    }

    public BalancedProvider(Provider<T> provider, ProviderChain next) {
        super(provider, next);
        start();
    }

    public void start() {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                    final long invokingCount = invokedCount.get();
                    final RegistryProvider registryProvider = ServiceFactory.takeInstance(CRegistryBean.getRegistryType(), RegistryProvider.class);
                    RegistryData registryData = registryProvider.readRegistryData(provider);
                    if (registryData != null) {
                        registryData.setInvokedCount(invokingCount);
                        registryProvider.onProviderDataChanges(provider, registryData);
                    }
                    LOGGER.debug("collect provider:{} invoking count:{}, RegistryData:{}", getKey(), invokingCount, GsonUtil.toJson(registryData));
                },
                ONE_MINUTE_SECOND,
                ONE_MINUTE_SECOND,
                TimeUnit.SECONDS);
    }

    @Override
    public void destroy() {
        scheduledThreadPoolExecutor.shutdown();
        LOGGER.debug("[destroy] BalancedProvider counting shutdown success.");
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

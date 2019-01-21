package lam.cobia.core;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description: NotNegativeLong
 * @author: linanmiao
 * @date: 2018/9/23 21:00
 * @version: 1.0
 */
public class NotNegativeLong implements Serializable {

    private static final long serialVersionUID = -6278982196157959377L;

    private final AtomicLong value;

    public NotNegativeLong() {
        value = new AtomicLong(0);
    }

    public NotNegativeLong(long initialValue) {
        if (initialValue < 0) {
            throw new IllegalArgumentException("initialValue(" + initialValue + ") < 0");
        }
        value = new AtomicLong(initialValue);
    }

    public long getAndIncrement() {
        long longs[] = compareAndAdd(Long.MAX_VALUE, 1);
        return longs[0];
    }

    public long incrementAndGet() {
        long longs[] = compareAndAdd(Long.MAX_VALUE, 1);
        return longs[1];
    }

    public long getAndDecrement() {
        long longs[] = compareAndAdd(0, -1);
        return longs[0];
    }

    public long decrementAndGet() {
        long longs[] = compareAndAdd(0, -1);
        return longs[1];
    }

    private long[] compareAndAdd(long boundary, long addStep) {
        long oldValue = get();
        long newValue = oldValue == boundary ? boundary : oldValue + addStep;
        while (!value.compareAndSet(oldValue, newValue)) {
            oldValue = get();
            newValue = oldValue == boundary ? boundary : oldValue + addStep;
        }
        return new long[] {oldValue, newValue};
    }

    public long get() {
        return value.get();
    }

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(
                10,
                10,
                0,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
                );
        NotNegativeLong notNegativeLong = new NotNegativeLong(10);

        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                System.out.println(notNegativeLong.getAndDecrement());
            });
        }
    }

}

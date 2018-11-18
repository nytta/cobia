package lam.cobia.core;

import java.io.Serializable;
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
        checkValue(initialValue, 0);
        value = new AtomicLong(initialValue);
    }

    private void checkValue(long value, long leastValue) {
        if (value < leastValue) {
            throw new IllegalStateException(value + " < " + leastValue);
        }
    }

    public long getAndIncrement() {
        return value.getAndIncrement();
    }

    public long incrementAndGet() {
        return value.incrementAndGet();
    }

    public long getAndDecrement() {
        long oldValue = value.getAndDecrement();
        checkValue(oldValue, 1);
        return oldValue;
    }

    public long decrementAndGet() {
        long newValue = value.decrementAndGet();
        checkValue(newValue, 0);
        return newValue;
    }

    public long get() {
        return value.get();
    }
}

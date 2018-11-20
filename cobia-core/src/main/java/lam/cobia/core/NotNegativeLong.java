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
        if (initialValue < 0) {
            throw new IllegalArgumentException("initialValue(" + initialValue + ") < 0");
        }
        value = new AtomicLong(initialValue);
    }

    public long getAndIncrement() {
        long oldValue = value.get();
        long newValue = oldValue == Long.MAX_VALUE ? 0 : oldValue + 1;
        while (!value.compareAndSet(oldValue, newValue)) {
            oldValue = value.get();
            newValue = oldValue == Long.MAX_VALUE ? 0 : oldValue + 1;
        }
        return oldValue;
    }

    public long incrementAndGet() {
        long oldValue = value.get();
        long newValue = oldValue == Long.MAX_VALUE ? 0 : oldValue + 1;
        while (!value.compareAndSet(oldValue, newValue)) {
            oldValue = value.get();
            newValue = oldValue == Long.MAX_VALUE ? 0 : oldValue + 1;
        }
        return newValue;
    }

    public long getAndDecrement() {
        long oldValue = value.get();
        long newValue = oldValue == 0 ? 0 : oldValue - 1;
        while (!value.compareAndSet(oldValue, newValue)) {
            oldValue = value.get();
            newValue = oldValue == 0 ? 0 : oldValue - 1;
        }
        return oldValue;
    }

    public long decrementAndGet() {
        long oldValue = value.get();
        long newValue = oldValue == 0 ? 0 : oldValue - 1;
        while (!value.compareAndSet(oldValue, newValue)) {
            oldValue = value.get();
            newValue = oldValue == 0 ? 0 : oldValue - 1;
        }
        return newValue;
    }

    public long get() {
        return value.get();
    }

}

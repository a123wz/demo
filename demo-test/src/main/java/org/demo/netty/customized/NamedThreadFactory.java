package org.demo.netty.customized;

import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2020年10月08日 09:38:00
 */
public class NamedThreadFactory implements ThreadFactory {

    private final static Map<String, AtomicInteger> PREFIX_COUNTER = new ConcurrentHashMap<>();
    private final String prefix;
    private final int totalSize;
    private final boolean makeDaemons;

    public NamedThreadFactory(String prefix, int totalSize, boolean makeDaemons) {
        PREFIX_COUNTER.putIfAbsent(prefix, new AtomicInteger(0));
        int prefixCounter = PREFIX_COUNTER.get(prefix).incrementAndGet();
        this.prefix = prefix + "_" + prefixCounter;
        this.makeDaemons = makeDaemons;
        this.totalSize = totalSize;
    }

    public NamedThreadFactory(String prefix, int totalSize) {
        this(prefix, totalSize, true);
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = prefix;
        if (totalSize > 1) {
            name += "_" + totalSize;
        }
        Thread thread = new FastThreadLocalThread(r, name);

        thread.setDaemon(makeDaemons);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}

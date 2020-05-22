package com.fungus_soft.desktop;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {

    public static final ExecutorService POOL = Executors.newFixedThreadPool(100, new ThreadFactory() {
        final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, String.format("AsyncThread %s", counter.incrementAndGet()));
        }
    });

    private static final ScheduledExecutorService RUNNABLE_POOL = Executors.newScheduledThreadPool(10, new ThreadFactory() {
        private final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncThread " + counter.incrementAndGet());
        }
    });

    public static MouseAdapter click(IClick e) {
        return new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e2) { e.click(e2); }
        };
    }

    public interface IClick {
        public void click(MouseEvent e);
    }

    public static String getJavaVersion() {
        double i = Double.valueOf(System.getProperty("java.class.version"));
        return (i - 44) + "&nbsp;<small>(" + System.getProperty("java.version") + ")</small>";
    }

    public static void schedule(Runnable r, long initialDelay, long delay, TimeUnit unit) {
        RUNNABLE_POOL.scheduleAtFixedRate(r, initialDelay, delay, unit);
    }

    public static void schedule(Runnable r, long delay, TimeUnit unit) {
        RUNNABLE_POOL.schedule(r, delay, unit);
    }

    /**
     * Exucutes the runnable async
     * 
     * @param runnable
     */
    public static void runAsync(Runnable runnable) {
        POOL.execute(runnable);
    }

    /**
     * Get the total number of currently running Threads
     */
    public static int getTotal() {
        return ((ThreadPoolExecutor) POOL).getActiveCount();
    }

}
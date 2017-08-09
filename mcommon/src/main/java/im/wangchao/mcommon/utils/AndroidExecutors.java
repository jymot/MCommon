package im.wangchao.mcommon.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Bolts-Android
 */
public class AndroidExecutors {
    /**
     * Nexus 5: Quad-Core
     * Moto X: Dual-Core
     *
     * AsyncTask:
     *   CORE_POOL_SIZE = CPU_COUNT + 1
     *   MAX_POOL_SIZE = CPU_COUNT * 2 + 1
     *
     * https://github.com/android/platform_frameworks_base/commit/719c44e03b97e850a46136ba336d729f5fbd1f47
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE_TIME = 1L;

    private static final AndroidExecutors INSTANCE = new AndroidExecutors();

    private final Executor uiThread;
    private final Executor background;

    private AndroidExecutors() {
        uiThread = new UIThreadExecutor();
        background = newCachedThreadPool();
    }

    /**
     * Creates a proper Cached Thread Pool. Tasks will reuse cached threads if available
     * or create new threads until the core pool is full. tasks will then be queued. If an
     * task cannot be queued, a new thread will be created unless this would exceed max pool
     * size, then the task will be rejected. Threads will time out after 1 second.
     *
     * Core thread timeout is only available on android-9+.
     *
     * @return the newly created thread pool
     */
    public static ExecutorService newCachedThreadPool() {
        ThreadPoolExecutor executor =  new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());

        allowCoreThreadTimeout(executor, true);

        return executor;
    }

    /**
     * Creates a proper Cached Thread Pool. Tasks will reuse cached threads if available
     * or create new threads until the core pool is full. tasks will then be queued. If an
     * task cannot be queued, a new thread will be created unless this would exceed max pool
     * size, then the task will be rejected. Threads will time out after 1 second.
     *
     * Core thread timeout is only available on android-9+.
     *
     * @param threadFactory the factory to use when creating new threads
     * @return the newly created thread pool
     */
    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        ThreadPoolExecutor executor =  new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);

        allowCoreThreadTimeout(executor, true);

        return executor;
    }

    /**
     * Compatibility helper function for
     * {@link ThreadPoolExecutor#allowCoreThreadTimeOut(boolean)}
     *
     * Only available on android-9+.
     *
     * @param executor the {@link ThreadPoolExecutor}
     * @param value true if should time out, else false
     */
    public static void allowCoreThreadTimeout(ThreadPoolExecutor executor, boolean value) {
        executor.allowCoreThreadTimeOut(value);
    }

    /**
     * An {@link AndroidExecutors} that executes tasks on the UI thread.
     */
    public static Executor uiThread() {
        return INSTANCE.uiThread;
    }

    public static Executor background() {
        return INSTANCE.background;
    }

    /**
     * An {@link AndroidExecutors} that runs tasks on the UI thread.
     */
    private static class UIThreadExecutor implements Executor {
        @Override public void execute(@NonNull Runnable command) {
            new Handler(Looper.getMainLooper()).post(command);
        }
    }
}

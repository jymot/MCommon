package im.wangchao.mcommon.log;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import im.wangchao.mcommon.utils.StringUtils;

/**
 * <p>Description  : LOG.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 16/6/27.</p>
 * <p>Time         : 下午4:28.</p>
 */
public class LOG {

    private static final List<AbsLog> LOGS = new ArrayList<>();
    private static final AbsLog[] LOG_ARRAY_EMPTY = new AbsLog[0];
    private static volatile AbsLog[] logAsArray = LOG_ARRAY_EMPTY;

    public static void addLog(AbsLog log){
        synchronized (LOGS){
            LOGS.add(log);
            logAsArray = LOGS.toArray(new AbsLog[LOGS.size()]);
        }
    }

    public static void removeLog(AbsLog log){
        synchronized (LOGS){
            LOGS.remove(log);
            if (LOGS.size() == 0){
                logAsArray = LOG_ARRAY_EMPTY;
            }
        }
    }

    /**
     * Clear all {@link AbsLog}
     */
    public static void clear(){
        synchronized (LOGS){
            LOGS.clear();
            logAsArray = LOG_ARRAY_EMPTY;
        }
    }

    /**
     * Default AbsLog
     */
    public static AbsLog getDefault(){
        return defaultLog;
    }

    /**
     * Log a verbose message.
     */
    public static void v(@NonNull String message) {
        v(StringUtils.EMPTY, message);
    }

    /**
     * Log a verbose message with tag.
     */
    public static void v(String tag, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log : array) {
            log.v(tag, message);
        }

    }

    /**
     * Log a verbose exception and a message.
     */
    public static void v(Throwable t, @NonNull String message) {
        v(StringUtils.EMPTY, t, message);
    }

    /**
     * Log a verbose exception and a message with tag.
     */
    public static void v(String tag, Throwable t, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array){
            log.v(tag, t, message);
        }
    }

    /**
     * Log a debug message.
     */
    public static void d(@NonNull String message) {
        d(StringUtils.EMPTY, message);
    }

    /**
     * Log a debug message with tag.
     */
    public static void d(String tag, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array){
            log.d(tag, message);
        }
    }

    /**
     * Log a debug exception and a message.
     */
    public static void d(Throwable t, @NonNull String message) {
        d(StringUtils.EMPTY, t, message);
    }

    /**
     * Log a debug exception and a message with tag.
     */
    public static void d(String tag, Throwable t, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.d(tag, t, message);
        }
    }

    /**
     * Log an info message.
     */
    public static void i(@NonNull String message) {
        i(StringUtils.EMPTY, message);
    }

    /**
     * Log an info message with tag.
     */
    public static void i(String tag, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.i(tag, message);
        }
    }

    /**
     * Log an info exception and a message.
     */
    public static void i(Throwable t, @NonNull String message) {
        i(StringUtils.EMPTY, t, message);
    }

    /**
     * Log an info exception and a message with tag.
     */
    public static void i(String tag, Throwable t, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.i(tag, t, message);
        }
    }

    /**
     * Log a warning message.
     */
    public static void w(@NonNull String message) {
        w(StringUtils.EMPTY, message);
    }

    /**
     * Log a warning message with tag.
     */
    public static void w(String tag, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.w(tag, message);
        }
    }

    /**
     * Log a warning exception and a message.
     */
    public static void w(Throwable t, @NonNull String message) {
        w(StringUtils.EMPTY, t, message);
    }

    /**
     * Log a warning exception and a message with tag.
     */
    public static void w(String tag, Throwable t, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.w(tag, t, message);
        }
    }

    /**
     * Log an error message.
     */
    public static void e(@NonNull String message) {
        e(StringUtils.EMPTY, message);
    }

    /**
     * Log an error message with tag.
     */
    public static void e(String tag, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.e(tag, message);
        }
    }

    /**
     * Log an error exception and a message.
     */
    public static void e(Throwable t, @NonNull String message) {
        e(StringUtils.EMPTY, t, message);
    }

    /**
     * Log an error exception and a message with tag.
     */
    public static void e(String tag, Throwable t, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.e(tag, t, message);
        }
    }

    /**
     * Log an assert message.
     */
    public static void wtf(@NonNull String message) {
        wtf(StringUtils.EMPTY, message);
    }

    /**
     * Log an assert message with tag.
     */
    public static void wtf(String tag, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.wtf(tag, message);
        }
    }

    /**
     * Log an assert exception and a message.
     */
    public static void wtf(Throwable t, @NonNull String message) {
        wtf(StringUtils.EMPTY, t, message);
    }

    /**
     * Log an assert exception and a message with tag.
     */
    public static void wtf(String tag, Throwable t, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.wtf(tag, t, message);
        }
    }

    /**
     * Log at {@code priority} a message.
     */
    public static void log(int priority, @NonNull String message) {
        log(StringUtils.EMPTY, priority, message);
    }

    /**
     * Log at {@code priority} a message with tag.
     */
    public static void log(String tag, int priority, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.log(tag, priority, message);
        }
    }

    /**
     * Log at {@code priority} an exception and a message.
     */
    public static void log(int priority, Throwable t, @NonNull String message) {
        log(StringUtils.EMPTY, priority, t, message);
    }

    /**
     * Log at {@code priority} an exception and a message with tag.
     */
    public static void log(String tag, int priority, Throwable t, @NonNull String message) {
        AbsLog[] array = logAsArray;

        for (AbsLog log: array) {
            log.log(tag, priority, t, message);
        }
    }

    //Default
    private static AbsLog defaultLog = new AbsLog() {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            switch (priority){
                case Log.VERBOSE:
                    if (t == null){
                        Log.v(tag, message);
                    } else {
                        Log.v(tag, message, t);
                    }
                    break;
                case Log.DEBUG:
                    if (t == null){
                        Log.d(tag, message);
                    } else {
                        Log.d(tag, message, t);
                    }
                    break;
                case Log.INFO:
                    if (t == null){
                        Log.i(tag, message);
                    } else {
                        Log.i(tag, message, t);
                    }
                    break;
                case Log.WARN:
                    if (t == null){
                        Log.w(tag, message);
                    } else {
                        Log.w(tag, message, t);
                    }
                    break;
                case Log.ERROR:
                    if (t == null){
                        Log.e(tag, message);
                    } else {
                        Log.e(tag, message, t);
                    }
                    break;
                case Log.ASSERT:
                    if (t == null){
                        Log.wtf(tag, message);
                    } else {
                        Log.wtf(tag, message, t);
                    }
                    break;
            }
        }
    };

    //Add default AbsLog
    static {
        addLog(defaultLog);
    }

}

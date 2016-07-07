package im.wangchao.mcommon.log;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import im.wangchao.mcommon.utils.StringUtils;

/**
 * <p>Description  : AbsLog.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 16/6/27.</p>
 * <p>Time         : 下午4:49.</p>
 */
public abstract class AbsLog {
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;
    public static final int ASSET = Log.ASSERT;
    public static final int CLOSE = Log.ASSERT + 1;

    private int logLevel;

    private String defaultTag;

    protected boolean isLoggable(int priority) {
        return priority >= logLevel;
    }

    public AbsLog setLogLevel(int priority){
        this.logLevel = priority;
        return this;
    }

    public AbsLog defaultTag(String defaultTag){
        this.defaultTag = defaultTag;
        return this;
    }

    protected String defaultTag(){
        return defaultTag;
    }

    /**
     * Log a verbose message.
     */
    public void v(String tag, String message) {
        println(tag, Log.VERBOSE, null, message);
    }

    /**
     * Log a verbose exception and a message.
     */
    public void v(String tag, Throwable t, String message) {
        println(tag, Log.VERBOSE, t, message);
    }

    /**
     * Log a debug message.
     */
    public void d(String tag, String message) {
        println(tag, Log.DEBUG, null, message);
    }

    /**
     * Log a debug exception and a message.
     */
    public void d(String tag, Throwable t, String message) {
        println(tag, Log.DEBUG, t, message);
    }

    /**
     * Log an info message.
     */
    public void i(String tag, String message) {
        println(tag, Log.INFO, null, message);
    }

    /**
     * Log an info exception and a message.
     */
    public void i(String tag, Throwable t, String message) {
        println(tag, Log.INFO, t, message);
    }

    /**
     * Log a warning message.
     */
    public void w(String tag, String message) {
        println(tag, Log.WARN, null, message);
    }

    /**
     * Log a warning exception and a message.
     */
    public void w(String tag, Throwable t, String message) {
        println(tag, Log.WARN, t, message);
    }

    /**
     * Log an error message.
     */
    public void e(String tag, String message) {
        println(tag, Log.ERROR, null, message);
    }

    /**
     * Log an error exception and a message.
     */
    public void e(String tag, Throwable t, String message) {
        println(tag, Log.ERROR, t, message);
    }

    /**
     * Log an assert message.
     */
    public void wtf(String tag, String message) {
        println(tag, Log.ASSERT, null, message);
    }

    /**
     * Log an assert exception and a message.
     */
    public void wtf(String tag, Throwable t, String message) {
        println(tag, Log.ASSERT, t, message);
    }

    /**
     * Log at {@code priority} a message.
     */
    public void log(String tag, int priority, String message) {
        println(tag, priority, null, message);
    }

    /**
     * Log at {@code priority} an exception and a message.
     */
    public void log(String tag, int priority, Throwable t, String message) {
        println(tag, priority, t, message);
    }

    /**
     * Uniform print
     */
    private void println(String tag, int priority, Throwable t, String message) {
        if (!isLoggable(priority)) {
            return;
        }
        if (StringUtils.isEmpty(tag)){
            tag = defaultTag();
        }
        if (message != null && message.length() == 0) {
            message = null;
        }
        if (message == null) {
            if (t == null) {
                return; // Log message if it's null and there's no throwable.
            }
            message = getStackTraceString(t);
        } else {
            if (t != null) {
                message += "\n" + getStackTraceString(t);
            }
        }

        log(priority, tag, message, t);
    }

    /**
     * Get StackTrace
     */
    private String getStackTraceString(Throwable t) {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }


    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See {@link android.util.Log} for constants.
     * @param tag Explicit or inferred tag.
     * @param message Formatted log message. May be {@code null}, but then {@code t} will not be.
     * @param t Accompanying exceptions. May be {@code null}, but then {@code message} will not be.
     */
    protected abstract void log(int priority, String tag, String message, Throwable t);

}

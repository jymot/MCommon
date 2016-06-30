package im.wangchao.mcommon.log;

import android.util.Log;
import android.util.SparseArray;

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

    //Tag
    private String tag;
    //Save each level is available
    private final SparseArray<Boolean> loggableArray = new SparseArray<>();

    public AbsLog tag(String tag){
        this.tag = tag;
        return this;
    }

    /**
     * Tag
     */
    protected String getTag() {
        return StringUtils.isEmpty(tag) ? this.getClass().getSimpleName() : tag;
    }

    protected boolean isLoggable(int priority) {
        if (loggableArray.size() == 0){
            return true;
        }
        return loggableArray.get(priority);
    }

    public AbsLog setLoggable(int priority, boolean loggable){
        synchronized (loggableArray){
            loggableArray.put(priority, loggable);
        }
        return this;
    }

    /**
     * Log a verbose message.
     */
    public void v(String message) {
        println(Log.VERBOSE, null, message);
    }

    /**
     * Log a verbose exception and a message.
     */
    public void v(Throwable t, String message) {
        println(Log.VERBOSE, t, message);
    }

    /**
     * Log a debug message.
     */
    public void d(String message) {
        println(Log.DEBUG, null, message);
    }

    /**
     * Log a debug exception and a message.
     */
    public void d(Throwable t, String message) {
        println(Log.DEBUG, t, message);
    }

    /**
     * Log an info message.
     */
    public void i(String message) {
        println(Log.INFO, null, message);
    }

    /**
     * Log an info exception and a message.
     */
    public void i(Throwable t, String message) {
        println(Log.INFO, t, message);
    }

    /**
     * Log a warning message.
     */
    public void w(String message) {
        println(Log.WARN, null, message);
    }

    /**
     * Log a warning exception and a message.
     */
    public void w(Throwable t, String message) {
        println(Log.WARN, t, message);
    }

    /**
     * Log an error message.
     */
    public void e(String message) {
        println(Log.ERROR, null, message);
    }

    /**
     * Log an error exception and a message.
     */
    public void e(Throwable t, String message) {
        println(Log.ERROR, t, message);
    }

    /**
     * Log an assert message.
     */
    public void wtf(String message) {
        println(Log.ASSERT, null, message);
    }

    /**
     * Log an assert exception and a message.
     */
    public void wtf(Throwable t, String message) {
        println(Log.ASSERT, t, message);
    }

    /**
     * Log at {@code priority} a message.
     */
    public void log(int priority, String message) {
        println(priority, null, message);
    }

    /**
     * Log at {@code priority} an exception and a message.
     */
    public void log(int priority, Throwable t, String message) {
        println(priority, t, message);
    }

    /**
     * Uniform print
     */
    private void println(int priority, Throwable t, String message) {
        if (!isLoggable(priority)) {
            return;
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

        log(priority, getTag(), message, t);
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
     * @param priority Log level. See {@link Log} for constants.
     * @param tag Explicit or inferred tag.
     * @param message Formatted log message. May be {@code null}, but then {@code t} will not be.
     * @param t Accompanying exceptions. May be {@code null}, but then {@code message} will not be.
     */
    protected abstract void log(int priority, String tag, String message, Throwable t);

}

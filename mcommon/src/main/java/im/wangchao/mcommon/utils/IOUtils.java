package im.wangchao.mcommon.utils;

import android.database.Cursor;

import java.io.Closeable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>Description  : IOUtils.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/11.</p>
 * <p>Time         : 下午3:09.</p>
 */
public class IOUtils {
    private IOUtils(){}

    /**
     * Closes {@code closeable}, ignoring any checked exceptions. Does nothing
     * if {@code closeable} is null.
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Closes {@code cursor}, ignoring any checked exceptions. Does nothing
     * if {@code cursor} is null.
     */
    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Closes {@code socket}, ignoring any checked exceptions. Does nothing if
     * {@code socket} is null.
     */
    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Closes {@code serverSocket}, ignoring any checked exceptions. Does nothing if
     * {@code serverSocket} is null.
     */
    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}

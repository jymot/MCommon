package im.wangchao.mcommon.utils.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import im.wangchao.mcommon.utils.StringUtils;

/**
 * <p>Description  : Digest.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/16.</p>
 * <p>Time         : 下午3:27.</p>
 */
public class DigestUtils {
    /**
     * 摘要码
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final int BUFFER_SIZE = 4096;

    private DigestUtils() {
        throw new AssertionError();
    }

    /**
     * encode String
     */
    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return new String(encodeHex(messageDigest.digest()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * encode File
     */
    public static String md5(File file){
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] b = md.digest();
            return new String(encodeHex(b));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

    /**
     * @return MD5 digest (32 characters)
     * @throws IOException
     */
    public static String md5(InputStream in) throws IOException {
        byte[] digest = digest(in, "MD5");
        return StringUtils.toHexString(digest, 32);
    }

    /**
     * @return SHA-1 digest (40 characters)
     * @throws IOException
     */
    public static String sha1(InputStream in) throws IOException {
        byte[] digest = digest(in, "SHA-1");
        return StringUtils.toHexString(digest, 40);
    }

    public static byte[] digest(InputStream in, String digestAlgo) throws IOException {
        MessageDigest digester;
        try {
            digester = MessageDigest.getInstance(digestAlgo);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }

        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            digester.update(buffer, 0, read);
        }
        return digester.digest();
    }

    /**
     * byte[] to hexadecimal char[]
     */
    protected static char[] encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }
}

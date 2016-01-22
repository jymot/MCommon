package im.wangchao.mcommon.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * <p>Description  : FileUtils.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 16/1/22.</p>
 * <p>Time         : 上午8:38.</p>
 */
public class FileUtils {
    private FileUtils() {
        throw new AssertionError();
    }

    /**
     * read File to bytes
     * @param file  File
     * @throws IOException
     */
    public static byte[] readBytes(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        try {
            return IOUtils.readBytes(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * write bytes to File
     * @param file      File
     * @param content   bytes
     * @throws IOException
     */
    public static void writeBytes(File file, byte[] content) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            out.write(content);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * read file to String, charset = UTF-8
     * @param file  File
     * @throws IOException
     */
    public static String readUtf8(File file) throws IOException {
        return readChars(file, "UTF-8");
    }

    /**
     * read file to String
     * @param file      File
     * @param charset   charset
     * @throws IOException
     */
    public static String readChars(File file, String charset) throws IOException {
        FileInputStream in = new FileInputStream(file);
        try {
            return IOUtils.readStr(in, charset);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * write chars to File, charset = UTF-8
     * @param file  File
     * @param text  chars
     * @throws IOException
     */
    public static void writeUtf8(File file, CharSequence text) throws IOException {
        writeChars(file, "UTF-8", text);
    }


    /**
     * write chars to File
     * @param file File
     * @param charset Charset
     * @param text  chars
     * @throws IOException
     */
    public static void writeChars(File file, String charset, CharSequence text) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        try{
            IOUtils.writeStr(out, text.toString(), charset);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * copy file
     * @throws IOException
     */
    public static void copyFile(File from, File to) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(from));
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(to));
            try {
                IOUtils.copy(in, out);
            } finally {
                IOUtils.closeQuietly(out);
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * copy file
     * @throws IOException
     */
    public static void copyFile(String fromFilename, String toFilename) throws IOException {
        copyFile(new File(fromFilename), new File(toFilename));
    }

    /**
     * read file to Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object readObject(File file) throws IOException,
            ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(fileIn));
        try {
            return in.readObject();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * write file to Object
     * @throws IOException
     */
    public static void writeObject(File file, Object object) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fileOut));
        try {
            out.writeObject(object);
            out.flush();
            // Force sync
            fileOut.getFD().sync();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * create file
     * @throws IOException
     */
    public static boolean createFile(File file) throws IOException {
        if (file == null){
            return false;
        }
        if (file.exists()){
            return true;
        }
        File parent = file.getParentFile();
        return createDir(parent) && file.createNewFile();
    }

    public static boolean createFile(String path) throws IOException {
        return StringUtils.isNotEmpty(path) && createFile(new File(path));
    }

    /**
     * create dir
     */
    public static boolean createDir(File file){
        return file != null && (file.exists() || file.mkdirs());
    }

    /**
     * create dir
     */
    public static boolean createDir(String path){
        return StringUtils.isNotEmpty(path) && createDir(new File(path));
    }

    /**
     * delete file
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists())
            return false;

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (File childFile : childFiles) {
                deleteFile(childFile);
            }
        }

        return file.delete();
    }

    /**
     * delete file
     */
    public static boolean deleteFile(String path){
        return StringUtils.isNotEmpty(path) && deleteFile(new File(path));
    }

}

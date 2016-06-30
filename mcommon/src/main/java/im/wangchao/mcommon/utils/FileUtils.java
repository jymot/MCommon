package im.wangchao.mcommon.utils;

import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
     * <p>read File to bytes</p>
     *
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
     * <p>write bytes to File</p>
     *
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
     * <p>read file to String, charset = UTF-8</p>
     *
     * @param file  File
     * @throws IOException
     */
    public static String readUtf8(File file) throws IOException {
        return readChars(file, "UTF-8");
    }

    /**
     * <p>read file to String</p>
     *
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
     * <p>write chars to File, charset = UTF-8</p>
     *
     * @param file  File
     * @param text  chars
     * @throws IOException
     */
    public static void writeUtf8(File file, CharSequence text) throws IOException {
        writeChars(file, "UTF-8", text);
    }

    /**
     * <p>write chars to File</p>
     *
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
     * append content to file
     * @throws IOException
     */
    public static void appendContentFile(String content, File file) throws IOException {
        if (StringUtils.isEmpty(content) || file == null || !file.exists()){
            return;
        }

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.append(content);
            bufferedWriter.flush();
        } finally {
            IOUtils.closeQuietly(bufferedWriter);
        }
    }

    /**
     * append content to file
     * @throws IOException
     */
    public static void appendContentFile(String content, String filePath) throws IOException {
        if (StringUtils.isEmpty(filePath)){
            return;
        }
        appendContentFile(content, new File(filePath));
    }

    /**
     * Create File
     * @return File
     * @throws IOException
     */
    @Nullable public static File createFile(File file) throws IOException {
        if (file == null){
            return null;
        }
        if (file.exists()){
            return file;
        }
        File parent = file.getParentFile();
        if (createDir(parent) != null && file.createNewFile()){
            return file;
        }
        return null;
    }

    /**
     * Create File
     * @param path File path
     * @return File
     * @throws IOException
     */
    @Nullable public static File createFile(String path) throws IOException {
        if (StringUtils.isEmpty(path)){
            return null;
        }
        return createFile(new File(path));
    }

    /**
     * Create Directory
     * @param file target file
     * @return  Directory
     */
    @Nullable public static File createDir(File file){
        if (file != null && (file.exists() || file.mkdirs())){
            return file;
        }
        return null;
    }

    /**
     * Create Directory
     * @param path target file path
     * @return  Directory
     */
    @Nullable public static File createDir(String path){
        if (StringUtils.isEmpty(path)){
            return null;
        }
        return createDir(new File(path));
    }

    /**
     * Delete File
     * @param file target file
     * @return  boolean
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
     * Delete File
     * @param path target file path
     * @return  boolean
     */
    public static boolean deleteFile(String path){
        return StringUtils.isNotEmpty(path) && deleteFile(new File(path));
    }

    /**
     * @return File suffix.
     */
    public static String getFileSuffix(String path){
        if (StringUtils.isEmpty(path)){
            return StringUtils.EMPTY;
        }
        return getFileSuffix(new File(path));
    }

    /**
     * @return File suffix.
     */
    public static String getFileSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory())
            return StringUtils.EMPTY;

        String name = file.getName();
        int lastIndex = name.lastIndexOf(".");
        if (name.startsWith(".") || lastIndex == -1)
            return StringUtils.EMPTY;

        return name.substring(lastIndex + 1, name.length());
    }

    /**
     * @return A file name that does not contain a suffix.
     */
    public static String getFileRealName(String path){
        if (StringUtils.isEmpty(path)){
            return StringUtils.EMPTY;
        }
        return getFileRealName(new File(path));
    }

    /**
     * @return A file name that does not contain a suffix.
     */
    public static String getFileRealName(File file) {
        if (file == null)
            return StringUtils.EMPTY;

        String name = file.getName();
        if (file.isDirectory()) {
            return name;
        }

        int index = name.lastIndexOf(".");
        String realName;
        if (name.startsWith(".") || index == -1) {
            realName = name;
        } else {
            realName = name.substring(0, name.lastIndexOf("."));
        }
        return realName;
    }
}

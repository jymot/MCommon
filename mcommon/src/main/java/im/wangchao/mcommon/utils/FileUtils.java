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
 * <p>Author       : wangchao.</p>
 * <p>Date         : 16/1/22.</p>
 * <p>Time         : 上午8:38.</p>
 */
public class FileUtils {
    private FileUtils() {
        throw new AssertionError();
    }

    /**
     * @return File exist.
     */
    public static boolean isExist(File file) {
        return file != null && file.exists();
    }

    /**
     * @return File exist.
     */
    public static boolean isExist(String filePath) {
        return StringUtils.isNotEmpty(filePath) && new File(filePath).exists();
    }

    /**
     * Read File to bytes.
     *
     * @return bytes
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
     * Read File to bytes.
     *
     * @return bytes
     * @throws IOException
     */
    public static byte[] readBytes(String filePath) throws IOException {
        return readBytes(new File(filePath));
    }

    /**
     * Write bytes to File.
     *
     * @throws IOException
     */
    public static void writeBytes(File file, byte[] content) throws IOException {
        //Write safely.
        createFile(file);

        OutputStream out = new FileOutputStream(file);
        try {
            out.write(content);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Write bytes to File.
     *
     * @throws IOException
     */
    public static void writeBytes(String filePath, byte[] content) throws IOException {
        writeBytes(new File(filePath), content);
    }

    /**
     * Read file to String with charset UTF-8.
     *
     * @throws IOException
     */
    public static String readUtf8(File file) throws IOException {
        return readChars(file, "UTF-8");
    }

    /**
     * Read file to String with charset UTF-8.
     *
     * @throws IOException
     */
    public static String readUtf8(String filePath) throws IOException {
        return readUtf8(new File(filePath));
    }

    /**
     * Read file to String.
     *
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
     * Read file to String.
     *
     * @throws IOException
     */
    public static String readChars(String filePath, String charset) throws IOException {
        return readChars(new File(filePath), charset);
    }

    /**
     * Write chars to File with charset UTF-8.
     *
     * @throws IOException
     */
    public static void writeUtf8(File file, CharSequence text) throws IOException {
        writeChars(file, "UTF-8", text);
    }

    /**
     * Write chars to File with charset UTF-8.
     *
     * @throws IOException
     */
    public static void writeUtf8(String filePath, CharSequence text) throws IOException {
        writeUtf8(new File(filePath),  text);
    }

    /**
     * Write chars to File.
     *
     * @throws IOException
     */
    public static void writeChars(File file, String charset, CharSequence text) throws IOException {
        //Write safely.
        createFile(file);

        FileOutputStream out = new FileOutputStream(file);
        try{
            IOUtils.writeStr(out, text.toString(), charset);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Write chars to File.
     *
     * @throws IOException
     */
    public static void writeChars(String filePath, String charset, CharSequence text) throws IOException {
        writeChars(new File(filePath), charset, text);
    }

    /**
     * Copy file.
     * @throws IOException
     */
    public static void copyFile(File from, File to) throws IOException {
        //Write safely.
        createFile(to);

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
     * Copy file.
     * @throws IOException
     */
    public static void copyFile(String fromFilename, String toFilename) throws IOException {
        copyFile(new File(fromFilename), new File(toFilename));
    }

    /**
     * Read file to Object.
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
     * Read file to Object.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object readObject(String filePath) throws IOException,
            ClassNotFoundException {
        return readObject(new File(filePath));
    }

    /**
     * Write file to Object.
     * @throws IOException
     */
    public static void writeObject(File file, Object object) throws IOException {
        //Write safely.
        createFile(file);

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
     * Write file to Object.
     * @throws IOException
     */
    public static void writeObject(String filePath, Object object) throws IOException {
        writeObject(new File(filePath), object);
    }

    /**
     * Append content to file.
     * @throws IOException
     */
    public static void appendContentFile(File file, String content) throws IOException {
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
     * Append content to file.
     * @throws IOException
     */
    public static void appendContentFile(String filePath, String content) throws IOException {
        if (StringUtils.isEmpty(filePath)){
            return;
        }
        appendContentFile(new File(filePath), content);
    }

    /**
     * Create File.
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
     * Create File.
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
     * Create Directory.
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
     * Create Directory.
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
     * Delete File.
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
     * Delete File.
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

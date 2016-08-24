package im.wangchao.mcommon.utils;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * <p>Description  : ZipUtils.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 16/6/29.</p>
 * <p>Time         : 上午11:07.</p>
 */
public class ZipUtils {
    private ZipUtils(){
        throw new AssertionError();
    }

    private static final int BUFF_SIZE = 1024 * 5;

    /**
     * Compress file to zip.
     * @return {@code true} if the file compress to zip, {@code false} otherwise.
     */
    public static boolean zip(@NonNull File inputFile, @NonNull File outputFile) throws Exception {
        if (!inputFile.exists())
            throw new FileNotFoundException("File (" + inputFile.getName() + ") that needs to be compressed does not exist.");

        // Create directory if not exist.
        File outputDir = outputFile.getParentFile();
        if (outputDir == null || (!outputDir.exists() && !outputDir.mkdirs())){
            throw new RuntimeException("Output file (" + outputFile.getName() + ") parent directory does not exist, or create a failed.");
        }

        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile));
            // Really zip.
            zip(inputFile, inputFile.getName(), zipOutputStream);
        } finally {
            IOUtils.closeQuietly(zipOutputStream);
        }
        return true;
    }

    /**
     * Compress file to zip.
     * @return {@code true} if the file compress to zip, {@code false} otherwise.
     */
    public static boolean zip(@NonNull String inputFilePath, @NonNull String outputFilePath) throws Exception {
        return StringUtils.isNotEmpty(inputFilePath) &&
                StringUtils.isNotEmpty(inputFilePath) &&
                zip(new File(inputFilePath), new File(outputFilePath));
    }

    /**
     * Real compression.
     */
    private static void zip(File originalFile, String entryName, ZipOutputStream zipOutputStream) throws Exception {
        if (originalFile.isFile()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(originalFile);
                ZipEntry zipEntry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(zipEntry);

                byte[] bytes = new byte[BUFF_SIZE];
                int len;
                while ((len = in.read(bytes)) != -1) {
                    zipOutputStream.write(bytes, 0, len);
                }
                zipOutputStream.flush();
            } finally {
                zipOutputStream.closeEntry();
                IOUtils.closeQuietly(zipOutputStream);
                IOUtils.closeQuietly(in);
            }
        } else {
            File[] files = originalFile.listFiles();
            if (files.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(originalFile.getName() + "/");
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.closeEntry();
            } else {
                final String pre = entryName.concat(File.separator);
                for (File childFile : files) {
                    zip(childFile, pre.concat(childFile.getName()), zipOutputStream);
                }
            }
        }
    }

    /**
     * Decompression file.
     * @return {@code true} if the file decompression success, {@code false} otherwise.
     */
    public static boolean unZip(@NonNull File zipFile, @NonNull String targetPath) throws IOException {
        if (!zipFile.exists()) throw new FileNotFoundException("File (" + zipFile.getName() + ") that needs to be decompressed does not exist.");

        if (StringUtils.isEmpty(targetPath)) {
            String zipParentPath = zipFile.getParent();
            if (StringUtils.isNotEmpty(zipParentPath)) {
                targetPath = zipParentPath + File.separator;
            }
        } else {
            targetPath = targetPath + File.separator;
        }

        File dir = new File(targetPath);
        if (dir.isFile()){
            throw new RuntimeException("Extract directory can not be a file.");
        }

        File result = FileUtils.createDir(dir);
        if (result == null){
            throw new RuntimeException("Target path (" + targetPath + ") create a failed.");
        }

        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            // Really unzip.
            unZip(targetPath, zipInputStream);
        } finally {
            if (zipInputStream != null) {
                zipInputStream.closeEntry();
                zipInputStream.close();
            }
        }
        return true;
    }

    /**
     * Decompression file.
     * @return {@code true} if the file decompression success, {@code false} otherwise.
     */
    public static boolean unZip(String zipFilePath, String targetPath) throws IOException {
        return StringUtils.isNotEmpty(zipFilePath) &&
                StringUtils.isNotEmpty(targetPath) &&
                unZip(new File(zipFilePath), targetPath);
    }

    /**
     * Real decompression.
     */
    private static void unZip(String targetPath, ZipInputStream zipInputStream) throws IOException {
        if (StringUtils.isNotEmpty(targetPath)) {
            targetPath = targetPath + File.separator;
        }

        ZipEntry entry;
        String entryName;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            entryName = entry.getName();
            if (entry.isDirectory()) {
                entryName = entryName.substring(0, entryName.length() - 1);
                FileUtils.createDir(targetPath + entryName);
            } else {
                File file = new File(targetPath + entryName);
                FileUtils.createFile(file);
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(file);
                    byte[] bytes = new byte[BUFF_SIZE];
                    int len;
                    while ((len = zipInputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, len);
                    }
                    fileOutputStream.flush();
                } finally {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                }
            }
        }
    }
}

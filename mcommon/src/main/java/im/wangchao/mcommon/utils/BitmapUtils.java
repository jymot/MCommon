package im.wangchao.mcommon.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Build;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Description  : BitmapUtils.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/12/13.</p>
 * <p>Time         : 上午9:46.</p>
 */
public class BitmapUtils {
    private BitmapUtils(){
        throw new AssertionError();
    }

    private final static LruCache<String, Bitmap> mMemoryCache;
    static {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    return bitmap.getAllocationByteCount() / 1024;
                } else {
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024; //also {@link Bitmap#getByteCount}
                }
            }
        };
    }

    /**
     * Cache bitmap to memory.
     */
    public static void addMemoryCache(String key, Bitmap bitmap){
        mMemoryCache.put(key, bitmap);
    }

    /**
     * @return Bitmap from memory cahce.
     */
    public static Bitmap getMemoryCache(String key){
        return mMemoryCache.get(key);
    }

    /**
     * @return Bitmap MimeType.
     */
    public static String mimeType(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options.outMimeType;
    }

    /**
     * @return Bitmap MimeType.
     */
    public static String mimeType(byte[] bytes){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        return options.outMimeType;
    }

    /**
     * @return Bitmap MimeType.
     */
    public static String mimeType(Resources res, int resId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        return options.outMimeType;
    }

    /**
     * @return The inSampleSize with {@code options} {@code reqWidth} {@code reqHeight}.
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * @return The best bitmap.
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * @return The best bitmap.
     */
    public static Bitmap decodeSampledBitmapFromResource(String filePath, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * @return The best bitmap.
     */
    public static Bitmap decodeSampledBitmapFromResource(byte[] bytes, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * @return Bitmap with raw resource.
     */
    public static Bitmap raw(Context context, int rawId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(rawId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * @return Rotate {@code angle} degrees bitmap. Default recycle the {@code originBitmap}.
     */
    public static Bitmap rotate(Bitmap originBitmap, int angle) {
        return rotate(originBitmap, angle, true);
    }

    /**
     * @return Rotate {@code angle} degrees bitmap.
     */
    public static Bitmap rotate(Bitmap originBitmap, int angle, boolean recycle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originBitmap,
                0, 0, originBitmap.getWidth(), originBitmap.getHeight(), matrix, true);
        if (recycle && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }
        return rotatedBitmap;
    }

    /**
     * @return Scale {@code originBitmap}. Default recycle the {@code originBitmap}.
     */
    public static Bitmap scale(Bitmap originBitmap, float scaleX, float scaleY) {
        return scale(originBitmap, scaleX, scaleY, true);
    }

    /**
     * @return Scale {@code originBitmap}.
     */
    public static Bitmap scale(Bitmap originBitmap, float scaleX, float scaleY, boolean recycle) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        Bitmap scaledBitmap = Bitmap.createBitmap(originBitmap,
                0, 0, originBitmap.getWidth(), originBitmap.getHeight(), matrix, true);
        if (recycle && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }
        return scaledBitmap;
    }

    /**
     * @return New bitmap with height {@code dstHeight} and width {@code dstWidth}.
     */
    public static Bitmap scale(Bitmap originBitmap, int dstWidth, int dstHeight, boolean recycle) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originBitmap, dstWidth, dstHeight, true);
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }
        return scaledBitmap;
    }

    /**
     * @return Thumbnail view.
     */
    public static Bitmap thumbnail(String path, int maxWidth, int maxHeight) {
        return thumbnail(path, maxWidth, maxHeight, false);
    }

    /**
     * @return Thumbnail view.
     */
    public static Bitmap thumbnail(String path, int maxWidth, int maxHeight, boolean autoRotate) {

        int angle = 0;
        if (autoRotate) {
            angle = exifRotateAngle(path);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;

        // Calculate InSampleSize
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        if (autoRotate && angle != 0) {
            bitmap = rotate(bitmap, angle);
        }

        return bitmap;
    }

    /**
     * Save bitmap.
     * @return Save path.
     */
    public static String save(Bitmap bitmap, Bitmap.CompressFormat format, int quality, File destFile) {
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            if (bitmap.compress(format, quality, out)) {
                out.flush();
                out.close();
            }

            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }

            return destFile.getAbsolutePath();
        } catch (Exception e) {
            // Silent
        }
        return null;
    }

    /**
     * @return Round bitmap.
     */
    public static Bitmap round(Bitmap originBitmap, int radius, boolean recycle) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, originBitmap.getWidth(), originBitmap.getHeight());
        RectF rectF = new RectF(new Rect(0, 0, originBitmap.getWidth(), originBitmap.getHeight()));

        Bitmap roundBitmap = Bitmap.createBitmap(originBitmap.getWidth(), originBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(originBitmap, rect, rect, paint);

        if (recycle && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        return roundBitmap;
    }

    /**
     * @return Circle bitmap.
     */
    public static Bitmap circle(Bitmap originBitmap, boolean recycle) {
        int min = originBitmap.getWidth() > originBitmap.getHeight() ? originBitmap.getHeight() : originBitmap.getWidth();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap circleBitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // Centered
        int left = - (originBitmap.getWidth() - min) / 2;
        int top = - (originBitmap.getHeight() - min) / 2;
        canvas.drawBitmap(originBitmap, left, top, paint);

        if (recycle && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        return circleBitmap;
    }

    /**
     * @return Gray bitmap.
     */
    public static Bitmap gray(Bitmap originBitmap, boolean recycle) {
        Bitmap grayBitmap = Bitmap.createBitmap(originBitmap.getWidth(),
                originBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(grayBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter =
                new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(originBitmap, 0, 0, paint);

        if (recycle && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        // Grayscale effect
        return grayBitmap;
    }

    /**
     * @return The bitmap of the EXIF rotate angle.
     */
    public static int exifRotateAngle(String path) {
        int angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
            }
        } catch (IOException e) {
            // Silent
        }
        return angle;
    }
}

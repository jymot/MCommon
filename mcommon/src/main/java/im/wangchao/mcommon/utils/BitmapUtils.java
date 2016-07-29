package im.wangchao.mcommon.utils;

import android.content.Context;
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

    /**
     * @return The inSampleSize with {@code options} {@code maxWidth} {@code maxHeight}.
     */
    public static int inSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        // raw height and width of image
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;

        // calculate best sample size
        int inSampleSize = 0;
        if (rawHeight > maxHeight || rawWidth > maxWidth) {
            float ratioWidth = (float) rawWidth / maxWidth;
            float ratioHeight = (float) rawHeight / maxHeight;
            inSampleSize = (int) Math.min(ratioHeight, ratioWidth);
        }
        inSampleSize = Math.max(1, inSampleSize);

        return inSampleSize;
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
        // 获取这个图片的宽和高, 此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        // 计算缩放比
        options.inSampleSize = inSampleSize(options, maxWidth, maxHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = BitmapFactory.decodeFile(path, options);

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
            //Silent
        }
        return null;
    }

    /**
     * @return Round bitmap.
     */
    public static Bitmap round(Bitmap originBitmap, int radius, boolean recycle) {
        // 准备画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // 准备裁剪的矩阵
        Rect rect = new Rect(0, 0, originBitmap.getWidth(), originBitmap.getHeight());
        RectF rectF = new RectF(new Rect(0, 0, originBitmap.getWidth(), originBitmap.getHeight()));

        Bitmap roundBitmap = Bitmap.createBitmap(originBitmap.getWidth(), originBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        // 这一句是核心，关于Xfermode和SRC_IN请自行查阅
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(originBitmap, rect, rect, paint);

        // 是否回收原始Bitmap
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

        // 居中显示
        int left = - (originBitmap.getWidth() - min) / 2;
        int top = - (originBitmap.getHeight() - min) / 2;
        canvas.drawBitmap(originBitmap, left, top, paint);

        // 是否回收原始Bitmap
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

        // 是否回收原始Bitmap
        if (recycle && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        //灰阶效果
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
            //Silent
        }
        return angle;
    }
}

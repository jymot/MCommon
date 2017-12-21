package im.wangchao.mcommon.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.util.TimeZone;

/**
 * <p>Description  : DeviceUtils.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 2017/12/21.</p>
 * <p>Time         : 下午4:11.</p>
 */
public class DeviceUtils {
    private DeviceUtils(){
        throw new AssertionError();
    }

    /**
     * Get the device's Universally Unique Identifier (UUID).
     */
    public String getUUID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Get the end-user-visible name for the end product.
     */
    public String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * Get the name of the overall product.
     */
    public String getProductName() {
        return android.os.Build.PRODUCT;
    }

    /**
     * Get the manufacturer of the product/hardware.
     */
    public String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * Get a hardware serial number.
     */
    public String getSerialNumber() {
        return android.os.Build.SERIAL;
    }

    /**
     * Get the OS version.
     */
    public String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * Get the SDK version.
     */
    public int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Get the time zone ID.
     */
    public String getTimeZoneID() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    /**
     * Whether it is virtual.
     */
    public boolean isVirtual() {
        return android.os.Build.FINGERPRINT.contains("generic") ||
                android.os.Build.PRODUCT.contains("sdk");
    }
}

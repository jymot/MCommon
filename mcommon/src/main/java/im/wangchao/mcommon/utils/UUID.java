package im.wangchao.mcommon.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * <p>Description  : UUID.</p>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 2018/1/12.</p>
 * <p>Time         : 下午2:07.</p>
 */
public class UUID {
    //从 Android M 开始，无法再通过第三方 API 获得本地设备 MAC 地址（例如，WLAN 和蓝牙）。
    //WifiInfo.getMacAddress() 方法和 BluetoothAdapter.getDefaultAdapter().getAddress() 方法都会返回 02:00:00:00:00:00。
    private static final String ANDROID_M_MAC = "02:00:00:00:00:00";

    private static String sUUID;

    private UUID(){
        throw new AssertionError();
    }

    public static String get(Context context){
        return get(context.getApplicationContext().getPackageName(), context);
    }

    /**
     * 获取 UUID
     * @param key 保存 UUID 的 key，如果当前 key 没有保存过 UUID，那么会重新获取
     * @param context 上下文
     */
    public static String get(String key, Context context){
        if (StringUtils.isNotEmpty(sUUID)){
            return sUUID;
        }

        String uuid = SharedPreferencesUtils.opt(context, key, "");

        if (StringUtils.isEmpty(uuid)){
            uuid = toUUID(DeviceUtils.getUUID(context));
        }

        if (StringUtils.isEmpty(uuid)){
            uuid = toUUID(getDeviceId(context));
        }

        if (StringUtils.isEmpty(uuid)){
            uuid = toUUID(getMac(context));
        }

        if (StringUtils.isEmpty(uuid)){
            uuid = toUUID(getCustom());
        }

        sUUID = uuid;
        SharedPreferencesUtils.put(context, key, sUUID);

        return sUUID;
    }

    private static String toUUID(String target){
        return java.util.UUID.nameUUIDFromBytes(target.getBytes()).toString();
    }

    private static String getCustom(){
        return "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
    }

    private static String getDeviceId(Context context){
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            return "";
        } else {
            if (manager == null){
                return "";
            }
            String deviceId = manager.getDeviceId();
            return deviceId == null ? "" : deviceId;
        }
    }

    private static String getMac(Context context) {
        String mac = null;
        if (Build.VERSION.SDK_INT >= 23) {
            mac = getMac60();
        } else {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            boolean canGetConnectionInfo = wifi != null && wifi.isWifiEnabled();
            if (canGetConnectionInfo){
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null){
                    mac = info.getMacAddress();
                }
            }
            if (TextUtils.isEmpty(mac) || ANDROID_M_MAC.equals(mac)) {
                mac = getMac60();
            }
        }
        //从 Android M 开始，无法再通过第三方 API 获得本地设备 MAC 地址（例如，WLAN 和蓝牙）。
        //WifiInfo.getMacAddress() 方法和 BluetoothAdapter.getDefaultAdapter().getAddress() 方法都会返回 02:00:00:00:00:00。
        if (ANDROID_M_MAC.equals(mac)) {
            mac = null;
        }

        return mac;
    }


    private static String getMac60() {
        String mac = getMac60_1();
        if (TextUtils.isEmpty(mac) || ANDROID_M_MAC.equals(mac)) {
            mac = getMac60_2();
        }
        return mac;
    }

    private static String getMac60_1() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            String temp = "";
            for (; null != temp; ) {
                temp = input.readLine();
                if (temp != null) {
                    macSerial = temp.trim();
                    break;
                }
            }
            if (StringUtils.isEmpty(macSerial)) {
                return FileUtils.readUtf8("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            }

            if (!StringUtils.isEmpty(macSerial)) {
                macSerial = macSerial.toLowerCase();
            }
        } catch (Exception ignore) {}
        return macSerial;
    }

    private static String getMac60_2() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                String mac = res1.toString();
                //转换成小写
                if (!TextUtils.isEmpty(mac)) {
                    mac = mac.toLowerCase();
                }
                return mac;
            }
        } catch (Exception ignore) {}
        return null;
    }
}

package com.sunland.netmodule.utils;//package com.sunlandgroup.utils;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.bluetooth.BluetoothAdapter;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
//import android.support.v4.app.ActivityCompat;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//import java.util.Locale;
//
///**
// * Created by LSJ on 2017/10/17.
// */
//
//public class PhoneUtils {
//    /**
//     * 获取当前手机系统语言。
//     *
//     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
//     */
//    public static String getSystemLanguage() {
//        return Locale.getDefault().getLanguage();
//    }
//
//    /**
//     * 获取当前系统上的语言列表(Locale列表)
//     *
//     * @return 语言列表
//     */
//    public static Locale[] getSystemLanguageList() {
//        return Locale.getAvailableLocales();
//    }
//
//    /**
//     * 获取当前手机系统版本号
//     *
//     * @return 系统版本号
//     */
//    public static String getSystemVersion() {
//        return android.os.Build.VERSION.RELEASE;
//    }
//
//    /**
//     * 获取手机型号
//     *
//     * @return 手机型号
//     */
//    public static String getSystemModel() {
//        return android.os.Build.MODEL;
//    }
//
//    /**
//     * 获取手机厂商
//     *
//     * @return 手机厂商
//     */
//    public static String getDeviceBrand() {
//        return android.os.Build.BRAND;
//    }
//
//    /**
//     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
//     *
//     * @return 手机IMEI
//     */
//    public static String getImei(Context ctx) {
//        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
//        if (tm != null) {
//            return tm.getDeviceId();
//        }
//        return null;
//    }
//
//    /*获取imsi1*/
//    public static String getImsi1(Context ctx) {
//        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
//        if (tm != null) {
//            return tm.getSubscriberId();
//        }
//        return null;
//    }
//
//    //获取GPS
//    public static void getGps(Activity activity) {
//        //第一步先获取LocationManager的对象
//        LocationManager GpsManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//
//        //通过LocationManager的对象来获取到Location的信息。
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        Location location = GpsManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        //Location中经常用到的有以下几种:
//
//        location.getAccuracy(); //   精度
//        location.getAltitude();  //  高度 : 海拔
//        location.getBearing();   //  导向
//        location.getSpeed();     //  速度
//        location.getLatitude();  //  纬度
//        location.getLongitude();  // 经度
//        location.getTime();      //  UTC时间 以毫秒计
//
//        Log.i("TAG------------>", "手机gps：" + location.getAccuracy() + "/" + location.getAltitude() + "/" + location.getBearing()
//                + "/" + location.getSpeed() + "/" + location.getLatitude() + "/" + location.getLongitude() + "/" + location.getTime());
//    }
//
//    /*网络连接是否已经连接*/
//    public static boolean isNetworkConnected(Context context) {
//        NetworkInfo info = ((ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//        if (info == null)
//            return false;
//        return info.isConnected();
//    }
//
//    /*获取手机网络信息*/
//    public static NetworkInfo getNetworkInfo(Context context){
//        return ((ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//    }
//
//    /*获取当前接入点名称,wifi下为接入点热点名，移动网络为apn名字*/
//    public static String getCurrentApnName(Context context) {
//        NetworkInfo info = ((ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//        if (info == null)
//            return "";
//        return info.getExtraInfo();
//    }
//
//    //获取手机IP地址
//    public static String getIPAddress(Context context) {
//        NetworkInfo info = ((ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//
//        Log.i("TAG------------>", "是否获取网络信息:" + info);
//
//        if (info != null && info.isConnected()) {
//            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
//                try {
//                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
//                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
//                        NetworkInterface intf = en.nextElement();
//                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                            InetAddress inetAddress = enumIpAddr.nextElement();
//                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                                return inetAddress.getHostAddress();
//                            }
//                        }
//                    }
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//
//            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
//                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                Log.i("TAG------------>", "是否获取无限网络" + wifiInfo);
//                String ipAddress = StringUtils.intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
//                return ipAddress;
//            }
//        } else {
//            //当前无网络连接,请在设置中打开网络
//        }
//        return null;
//    }
//
//    /*wifi是否打开*/
//    public static boolean isOpenWif(Context context){
//        try {
//            return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /*蓝牙是否开启*/
//    public static boolean isOpenBluetooth(){
//        try {
//            return BluetoothAdapter.getDefaultAdapter().isEnabled();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /*获取手机总内存（RAM）*/
//    public static long getTotalRAM(Context context){
//        try {
//            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
//            am.getMemoryInfo(mi);
//            return mi.totalMem;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    /*获取手机可用内存（RAM）*/
//    public static long getAvailRAM(Context context){
//        try {
//            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
//            am.getMemoryInfo(mi);
//            return mi.availMem;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    /*获取手机mac地址*/
//    public static String getMacAddress(Context context){
//        String mac = "";
//        try {
//            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            if(wifiInfo.getMacAddress()!=null) {
//                mac= wifiInfo.getMacAddress();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return mac;
//    }
//
//    // 获取CPU
//    public static String getCpu() {
//        try {
//            FileReader fr = new FileReader("/proc/cpuinfo");
//            BufferedReader br = new BufferedReader(fr);
//            String text = br.readLine();
//            String[] array = text.split(":\\s+", -1);
//            if(array.length >=2 )
//                return array[1];
//            else
//                return text;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//}

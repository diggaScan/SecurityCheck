package com.sunland.netmodule;

import android.os.Build;
import android.os.Environment;

/**
 * Created by LSJ on 2017/10/17.
 */

public class Global {
    public final static String appName = "hzydjw";
    public final static String sdPath = Environment.getExternalStorageDirectory().getPath();

    //    public final static String ip = "sunlandgroup.51vip.biz";
//    public static String port = "9080";
    public final static String postfix = "/hzydjw/services/IHzydjwService";

    public final static String ip = "20.65.2.12";
    public static String port = "3565";
    //本机信息
    public static String imei = "";
    public static String imsi1 = " ";
    public static String imsi2 = "";
    public static String gpsX = "";//经度
    public static String gpsY = "";//纬度
    public static String gpsinfo = gpsX + gpsY;

    public static String brand = Build.BRAND;
    public static String model = Build.MODEL + " " + Build.VERSION.SDK_INT;

}

package com.sunland.netmodule.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.io.File;
import java.util.List;

/**
 * Created by LSJ on 2017/10/17.
 */

public class AppUtils {
//    /**
//     * 获取安装的应用
//     *
//     * @param context
//     * @return
//     */
//    public static List<AppInfo> getInstalledAppInfo(Context context) {
//        List<AppInfo> list = new ArrayList<>();
//        PackageManager pm = context.getPackageManager();
//        List<PackageInfo> packs = pm.getInstalledPackages(0);
//        for (PackageInfo pi : packs) {
//       //     if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
//                AppInfo info = new AppInfo();
//                info.setAppname((String) pi.applicationInfo.loadLabel(pm));
//                info.setPkgname(pi.applicationInfo.packageName);
//                info.setVercode(pi.versionCode + "");
//                info.setVername(pi.versionName);
//                list.add(info);
////            }
//        }
//        return list;
//    }

    /**
     * 获取当前应用的版本号
     *
     * @param context 当前上下文对象
     */
    public static int getCurVersionCode(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 获取当前应用的版本号
     *
     * @param context 当前上下文对象
     */
    public static String getCurVersionName(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取指定APK文件的应用版本号
     *
     * @param context     当前上下文对象
     * @param apkFileName APK完整文件名
     */
    public static int getApkVersionCode(Context context, String apkFileName) {
        if (context != null && FileUtils.existsFile(apkFileName)) {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkFileName, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                return info.versionCode;
            }
        }
        return -1;
    }

    /**
     * 安装APK应用
     *
     * @param context     当前上下文对象
     * @param apkFileName APK完整文件名
     */
    public static void install(Context context, String apkFileName) {
        if (context != null && FileUtils.existsFile(apkFileName)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(apkFileName)), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /**
     * 隐式转显示
     *
     * @param context
     * @return
     */
    public static Intent createExplicitIntent(Context context, String action) {
        Intent implicitIntent = new Intent();
        implicitIntent.setAction(action);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        if (resolveInfo == null || resolveInfo.size() < 1) {
            return null;
        }

        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}

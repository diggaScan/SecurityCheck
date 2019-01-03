package com.sunland.securitycheck;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.sunland.securitycheck.crash.CrashApplication;


public class MyApplication extends CrashApplication {

    private String app_flavour;

    @Override
    public void onCreate() {
        super.onCreate();
        setPhoneInfo();
        getFlavour();

    }

    private void getFlavour() {
        PackageManager pm = getPackageManager();
        ApplicationInfo info = new ApplicationInfo();
        try {
            info = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        app_flavour = info.metaData.getString("flavour");

    }

    //广达市场版
    public boolean isAppCyber() {
        return app_flavour.equals("appCyber");
    }

    //独立应用版
    public boolean isIsoApp() {
        return app_flavour.equals("app");
    }

    //正式发布版
    public boolean isReleaseVersion() {
        return (app_flavour.equals("appCyber") || app_flavour.equals("app"));
    }

    private void setPhoneInfo() {
        TelephonyManager tpm = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            V_config.imei = tpm.getDeviceId();
            V_config.imsi1 = tpm.getSubscriberId();
        }
    }

}

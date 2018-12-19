package com.sunland.securitycheck;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.sunland.netmodule.Global;
import com.sunland.securitycheck.crash.CrashApplication;


public class MyApplication extends CrashApplication {

    private String app_flavour;

    @Override
    public void onCreate() {
        super.onCreate();
        getPhoneInfo();
        getFlavour();

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
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

    public boolean isAppCyber() {
        if (app_flavour == null)
            return false;
        return app_flavour.equals("appCyber");
    }

    public boolean isReleaseVersion() {
        if (app_flavour == null)
            return false;
        if (app_flavour.equals("appCyber") || app_flavour.equals("app")) {
            return true;
        } else
            return false;
    }

    private void getPhoneInfo() {
        TelephonyManager tpm = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Global.imei = tpm.getDeviceId();
            Global.imsi1 = tpm.getSubscriberId();
        }
    }
}

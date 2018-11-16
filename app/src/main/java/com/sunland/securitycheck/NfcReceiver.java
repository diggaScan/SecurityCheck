package com.sunland.securitycheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sunland.securitycheck.activities.Ac_check;

public class NfcReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ((Ac_check) context).onReceiveDate(intent);
    }

    public interface OnGetNfcDataListener {
        public void onReceiveDate(Intent intent);
    }
}

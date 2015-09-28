package com.sysmagic.plugins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sysmagic.mob.R;

public class ScanActivity extends Activity {
    public static ScanActivity instance = null;
    private String cmd = "scan";
    private String activity = "com.sysmagic.plugins.ScanActivity";
    public String TAG = "ScanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Intent ac = new Intent();
        ac.setAction("com.sysmagic.plugins.ScanService");
        ac.putExtra("activity", activity);
        sendBroadcast(ac);
        Log.e(TAG, "send broadcast");
        Intent sendToservice = new Intent(ScanActivity.this, ScanService.class);
        sendToservice.putExtra("cmd", cmd);
        this.startService(sendToservice);
        Log.e(TAG, "send to ScanService");
        Intent intent = new Intent();
        intent.putExtra("barcode","");
        setResult(RESULT_OK, intent);
        finish();
    }
}
/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.sysmagic.mob;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.sysmagic.plugins.ScanActivity;
import com.sysmagic.plugins.ScanService;

import org.apache.cordova.CordovaActivity;
import org.xwalk.core.JavascriptInterface;

public class MainActivity extends CordovaActivity
{
    public String TAG = "MainActivity";
    private MyBroadcast myBroad;
    private MediaPlayer mPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        getWindow().addFlags(0x80000000);
        super.onCreate(savedInstanceState);
        myBroad = new MyBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sysmagic.plugins.ScanActivity");
        registerReceiver(myBroad, filter);
        Log.e(TAG, "register Receiver");
        Intent start = new Intent(MainActivity.this, ScanService.class);
        MainActivity.this.startService(start);
        Log.e(TAG, "start first time service.");
        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(myBroad);
        Intent stopService = new Intent();
        stopService.setAction("com.sysmagic.plugins.ScanService");
        stopService.putExtra("stopflag", true);
        sendBroadcast(stopService);
        Log.e(TAG, "send stop");
        super.onDestroy();
    }
    /*
    @JavascriptInterface
    public void returnBarCode(String barcode){
        loadUrl("javascript:returnBarCode("+barcode+")");
    }

    private void returnBarCode(String barcode){
        ScanActivity.instance.setBarCode(barcode);
        ScanActivity.instance.finish();
    }
    */
    private class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedata = intent.getStringExtra("result");
            if (receivedata != null) {
                Log.e(TAG + "  receivedata", receivedata);
                //Toast.makeText(MainActivity.this, receivedata, Toast.LENGTH_LONG).show();
                //returnBarCode(receivedata);
                ScanActivity.instance.setBarCode(receivedata);
                ScanActivity.instance.finish();
                mPlayer = MediaPlayer.create(MainActivity.this, R.raw.msg);
                if(mPlayer.isPlaying()){
                    return;
                }
                mPlayer.start();
            }
        }
    }
}

package com.sysmagic.plugins;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import com.example.scandemo.SerialPort;

public class ScanService extends Service {
    private SerialPort mSerialPort;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private MyReceiver myReceive;
    private ReadThread mReadThread;
    private Timer sendData;
    private Timer scan100ms;
    public String activity = null;
    public String data;
    public StringBuffer data_buffer = new StringBuffer();
    private boolean run = true;
    private boolean run_scan100ms = false;
    private Timer timeout = null;
    public String TAG = "ScanService";
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        init();
    }
    private void init(){
        Log.e("service on create", "service on create");
        try {
            mSerialPort = new SerialPort(0, 9600, 0);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mSerialPort.scaner_poweron();
        Log.e(TAG, "scan power on");
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        myReceive = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sysmagic.plugins.ScanService");
        registerReceiver(myReceive, filter);
        sendData = new Timer();
        scan100ms = new Timer();
        mReadThread = new ReadThread();
        mReadThread.start();
        Log.e(TAG, "start thread");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "start command");
        String cmd_arr = intent.getStringExtra("cmd");
        if (cmd_arr == null)
            return 0;
        Log.e("CMD", cmd_arr);
        if("scan".equals(cmd_arr)){
            scan100ms.cancel();
            run_scan100ms = false;
            if(mSerialPort.scaner_trig_stat() == true){
                mSerialPort.scaner_trigoff();
            }
            mSerialPort.scaner_trigon();
            if(timeout != null){
                timeout.cancel();
                timeout = null;
                return 0;
            }
            timeout = new Timer();
            timeout.schedule(new TimerTask() {

                @Override
                public void run() {
                    mSerialPort.scaner_trigoff();
                    timeout = null;
                }
            }, 5000);
            Log.e(TAG, "start scan");
        }else if("toscan100ms".equals(cmd_arr)){
            if(run_scan100ms) return 0;
            run_scan100ms = true;
            scan100ms.cancel();
            scan100ms = new Timer();
            scan100ms.schedule(new TimerTask() {  //???Timer?????100ms??????
                @Override
                public void run() {
                    if(mSerialPort.scaner_trig_stat() == true){
                        mSerialPort.scaner_trigoff();
                    }
                    mSerialPort.scaner_trigon();
                }
            }, 0, 100);

        }
        return 0;
    }
    @Override
    public void onDestroy() {
        if (mReadThread != null)
            run = false;
        scan100ms.cancel();
        mSerialPort.scaner_poweroff();
        mSerialPort.close(14);
        unregisterReceiver(myReceive);
        super.onDestroy();
    }
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (run) {
                int size;
                try {
                    byte[] buffer = new byte[512];
                    if (mInputStream == null)
                        return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        data = new String(buffer, 0, size);
                        data_buffer.append(data);
                        Log.e(TAG, size +"********"+data);
                        data = null;
                        if(data_buffer != null && data_buffer.length() != 0 && activity != null){
                            Log.e("ScanService data", data_buffer.toString());
                            Intent serviceIntent = new Intent();
                            serviceIntent.setAction("com.sysmagic.plugins.ScanActivity");
                            serviceIntent.putExtra("result", data_buffer.toString());
                            data_buffer.setLength(0);
                            Log.e(TAG, "result");
                            sendBroadcast(serviceIntent);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String ac = intent.getStringExtra("activity");
            if(ac!=null)
                Log.e("receive activity", ac);
            activity = ac;
            if (intent.getBooleanExtra("stopflag", false))
                stopSelf();
            Log.e("stop service", intent.getBooleanExtra("stopflag", false)+ "");
        }
    }
}

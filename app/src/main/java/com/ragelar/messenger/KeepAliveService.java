package com.ragelar.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class KeepAliveService extends Service {
    public KeepAliveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Handler handler = new Handler();


            handler.postDelayed(new Runnable() {
               @Override
                public void run() {
                    CommunicatorClient.sendKeepAliveRequest();
                }
             }, 1000);
        }


}
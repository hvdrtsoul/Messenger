package com.ragelar.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
        Runnable keepAliveTask = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonResponse = CommunicatorClient.sendKeepAliveRequest();

                try {
                    if(jsonResponse.getString("result").equals("OK")) {
                        handler.postDelayed(this, 3000);
                        Toast.makeText(KeepAliveService.this, "KEEP ALIVE", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        handler.postDelayed(this, 3000);
                        PreferenceManager preferenceManager = new PreferenceManager(KeepAliveService.this);
                        preferenceManager.setSharedKey("undefined");
                        Toast.makeText(KeepAliveService.this, "CANNOT KEEP CONNECTION ALIVE. PLEASE, CHECK INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        keepAliveTask.run();


    }
}
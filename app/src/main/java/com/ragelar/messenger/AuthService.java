package com.ragelar.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class AuthService extends Service {
    public AuthService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Handler handler = new Handler();
        Runnable authTask = new Runnable() {
            @Override
            public void run() {

                String challenge = "";
                PreferenceManager preferenceManager = new PreferenceManager(AuthService.this);
                String userNameText = preferenceManager.getUserName();
                String sharedKey = preferenceManager.getSharedKey();

                JSONObject jsonResponse = CommunicatorClient.sendAuthRequest(userNameText, sharedKey);
                try{
                    if(jsonResponse.getString("result").equals("OK")){
                        JSONObject data = jsonResponse.getJSONObject("data");
                        challenge = data.getString(Constants.AUTH_CHALLENGE_HEADER);
                    }
                    else{
                       Toast.makeText(AuthService.this, "Не могу авторизоваться на сервере. Проверьте подключение к интернету или войдите в систему заново", Toast.LENGTH_LONG);
                        handler.postDelayed(this, 300_000);
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(AuthService.this, "Не могу авторизоваться на сервере. Проверьте подключение к интернету или войдите в систему заново", Toast.LENGTH_LONG);
                    handler.postDelayed(this, 300_000);
                    return;
                }

                ANomalUSProvider anomalusProvider = new ANomalUSProvider();
                Sanitizer sanitizer = new Sanitizer();

                BigInteger privateKey = new BigInteger(preferenceManager.getKeyPrivateKey());

                byte[] decodedBytes = anomalusProvider.decodeBytes(sanitizer.unSanitize(challenge), privateKey);

                jsonResponse = CommunicatorClient.sendTwistedRequest(userNameText, new String(decodedBytes, StandardCharsets.UTF_8), sharedKey);

                try{
                    if(jsonResponse.getString("result").equals("OK")){
                        JSONObject data = jsonResponse.getJSONObject("data");
                        String newSession = data.getString(Constants.TWISTED_NEW_SESSION_HEADER);

                        preferenceManager.setSession(newSession);
                        Toast.makeText(AuthService.this, "Авторизация прошла успешно", Toast.LENGTH_LONG).show();
                        handler.postDelayed(this, 600_000);
                        return;
                    }
                    else{

                    }
                } catch (JSONException e) {
                    Toast.makeText(AuthService.this, "Не могу авторизоваться на сервере. Проверьте подключение к интернету или войдите в систему заново", Toast.LENGTH_LONG);
                    handler.postDelayed(this, 300_000);
                    return;
                }

            }
        };
        authTask.run();
    }
}
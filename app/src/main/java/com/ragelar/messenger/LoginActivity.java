package com.ragelar.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

public class LoginActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void establishConnection(){
        DFHProvider dfhProvider = new DFHProvider();
        BigInteger privateKey = dfhProvider.generatePrivateKey();
        BigInteger publicKey = dfhProvider.generatePublicKey(privateKey);

        JSONObject jsonResponse = CommunicatorClient.sendMeetRequest(publicKey.toString());

        String resultHeader = null;
        try {
            resultHeader = jsonResponse.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
            preferenceManager.setSharedKey("undefined");
            return;
        }

        if(resultHeader.equals("ERR")){
            preferenceManager.setSharedKey("undefined");
            return;
        }

        String serverPublicKey = null;
        try {
            JSONObject data = jsonResponse.getJSONObject("data");
            serverPublicKey = data.getString("publicKey");
        } catch (JSONException e) {
            e.printStackTrace();
            preferenceManager.setSharedKey("undefined");
            return;
        }

        BigInteger sharedKey = dfhProvider.generateSharedKey(privateKey, new BigInteger(serverPublicKey));

        preferenceManager.setSharedKey(sharedKey.toString());
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceManager = new PreferenceManager(LoginActivity.this);
        //preferenceManager.clearSession();
        if(!preferenceManager.isLoggedIn()) {
            preferenceManager.clearSession();
            establishConnection();
            SystemClock.sleep(3000);
            while (preferenceManager.getSharedKey().equals("undefined")) {
                SystemClock.sleep(5000);
                showToast("Невозможно установить безопасное соединение с сервером. Пробую снова...");
                establishConnection();
            }
            Intent intention = new Intent(LoginActivity.this, KeepAliveService.class);
            startService(intention);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(preferenceManager.isLoggedIn()){
            Intent intention = new Intent(LoginActivity.this, MainActivity.class);
            intention.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intention);

            //finish();
        }
    }
}
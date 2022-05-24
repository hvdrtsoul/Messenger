package com.ragelar.messenger;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(MainActivity.this);

        //preferenceManager.clearSession();

            establishConnection();
            //SystemClock.sleep(3000);
            while (preferenceManager.getSharedKey().equals("undefined")) {
                //SystemClock.sleep(5000);
                showToast("Невозможно установить безопасное соединение с сервером. Пробую снова...");
                establishConnection();
            }
            Intent intention1 = new Intent(MainActivity.this, KeepAliveService.class);
            startService(intention1);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMainContainer);
        NavController navController = hostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnItemReselectedListener(item -> {
            int newDestination = item.getItemId();
            navController.popBackStack(newDestination, false);
        });

        Intent intention = new Intent(MainActivity.this, AuthService.class);
        startService(intention);

        GetMessagesHandler getMessagesHandler = new GetMessagesHandler(this.getApplicationContext());
        getMessagesHandler.start();

    }

    @Override
    protected void onResume() {
        super.onResume();

        String userName = preferenceManager.getUserName();

        //Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();


    }
/*
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Вы действительно хотите выйти?").setPositiveButton("ДА",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                }).setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();

 */
    }





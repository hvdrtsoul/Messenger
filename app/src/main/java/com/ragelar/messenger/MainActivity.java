package com.ragelar.messenger;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(MainActivity.this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragmentMainContainer);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        Intent intention = new Intent(MainActivity.this, AuthService.class);
        startService(intention);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String userName = preferenceManager.getUserName();

        Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();


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





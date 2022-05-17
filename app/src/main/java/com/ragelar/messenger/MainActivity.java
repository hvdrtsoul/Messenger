package com.ragelar.messenger;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String userName = preferenceManager.getUserName();

        Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();


    }

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
    }




}
package com.ragelar.messenger;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context context;
    private static DatabaseClient instance;

    private AppDatabase appDatabase;

    private DatabaseClient(Context context){
        this.context = context;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, Constants.LOCAL_DATABASE_NAME).fallbackToDestructiveMigration().build();
    }

    public static DatabaseClient getInstance(Context context){
        if(instance == null){
            instance = new DatabaseClient(context);
        }

        return instance;
    }

    public AppDatabase getAppDatabase(){
        return appDatabase;
    }
}

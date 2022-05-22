package com.ragelar.messenger;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Dialog.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DialogDao dialogDao();
}

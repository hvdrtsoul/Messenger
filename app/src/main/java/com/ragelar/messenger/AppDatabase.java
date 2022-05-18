package com.ragelar.messenger;

import androidx.room.Database;

@Database(entities = {Dialog.class}, version = 1)
public abstract class AppDatabase {
    public abstract DialogDao dialogDao();
}

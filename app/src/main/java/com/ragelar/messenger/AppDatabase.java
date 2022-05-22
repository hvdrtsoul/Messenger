package com.ragelar.messenger;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Dialog.class, Message.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DialogDao dialogDao();
    public abstract MessageDao messageDao();
}

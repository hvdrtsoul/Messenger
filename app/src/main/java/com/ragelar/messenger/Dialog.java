package com.ragelar.messenger;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Dialog {
    @PrimaryKey
    public int dialogId;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "sharedKey")
    public String sharedKey;
}

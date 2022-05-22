package com.ragelar.messenger;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Dialog {
    @PrimaryKey(autoGenerate = true)
    public int dialogId;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "sharedKey")
    public String sharedKey;

    @ColumnInfo(name = "lastMessage")
    public String lastMessage;

    @ColumnInfo(name = "privateKey")
    public String privateKey;

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSharedKey() {
        return sharedKey;
    }

    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}

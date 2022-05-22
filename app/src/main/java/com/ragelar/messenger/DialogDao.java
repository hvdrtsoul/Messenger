package com.ragelar.messenger;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DialogDao {
    @Query("SELECT * FROM dialog")
    List<Dialog> getAll();

    @Query("SELECT dialogId FROM dialog WHERE user_name = :userName")
    Dialog findId(String userName);

    @Query("UPDATE dialog SET lastMessage =:lastMessage WHERE user_name = :userName")
    void setLastMessage(String userName, String lastMessage);

    @Query("UPDATE dialog SET sharedKey = :newSharedKey WHERE user_name = :userName")
    void setSharedKey(String userName, String newSharedKey);

    @Query("SELECT EXISTS(SELECT * FROM dialog WHERE user_name = :userName)")
    boolean dialogExists(String userName);

    @Query("SELECT privateKey FROM dialog WHERE user_name = :userName")
    String getPrivateKeyForUser(String userName);

    @Query("SELECT sharedKey FROM dialog WHERE user_name = :userName")
    String getSharedKeyForUser(String userName);

    @Insert
    void addDialog(Dialog dialog);

    @Delete
    void removeDialog(Dialog dialog);
}

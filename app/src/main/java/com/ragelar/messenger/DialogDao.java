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
    Dialog findBYId(String userName);

    @Insert
    void addDialog(Dialog dialog);

    @Delete
    void removeDialog(Dialog dialog);
}

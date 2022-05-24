package com.ragelar.messenger;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message")
    List<Message> getAll();

    @Query("SELECT * FROM message WHERE `from` = :userName OR `to` = :userName")
    List<Message> getMessagesFromUser(String userName);

    @Insert
    void addMessage(Message message);
}

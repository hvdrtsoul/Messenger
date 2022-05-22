package com.ragelar.messenger;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int messageId;

    @ColumnInfo(name = "from")
    public String from;

    @ColumnInfo(name = "to")
    public String to;

    @ColumnInfo(name = "message_type")
    public String messageType;

    @ColumnInfo(name = "timestamp")
    public Long timestamp;

    @ColumnInfo(name = "data")
    public String data;

    public int getMessageId() {
        return messageId;
    }

    public String getFrom() {
        return from;
    }

    public String getMessageType() {
        return messageType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

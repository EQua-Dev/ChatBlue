package com.androidStrike.bluetoothchatapp;

import android.text.format.Time;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "received_messages")
public class ReceivedMessages {
    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "message_format")
    private String message;

    @ColumnInfo(name = "message")
    private char messageReceived;

    @ColumnInfo(name = "received_from_uid")
    private int receivedFromUid;

    @ColumnInfo(name = "received_from_chat_name")
    private String receivedFromChatName;

    @ColumnInfo(name = "date_received")
    private Date dateReceived;

    @ColumnInfo(name = "time_received")
    private Time timeReceived;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public char getMessageReceived() {
        return messageReceived;
    }

    public void setMessageReceived(char messageReceived) {
        this.messageReceived = messageReceived;
    }

    public int getReceivedFromUid() {
        return receivedFromUid;
    }

    public void setReceivedFromUid(int receivedFromUid) {
        this.receivedFromUid = receivedFromUid;
    }

    public String getReceivedFromChatName() {
        return receivedFromChatName;
    }

    public void setReceivedFromChatName(String receivedFromChatName) {
        this.receivedFromChatName = receivedFromChatName;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public Time getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(Time timeReceived) {
        this.timeReceived = timeReceived;
    }
}

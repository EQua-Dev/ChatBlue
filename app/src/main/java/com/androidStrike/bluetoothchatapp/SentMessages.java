package com.androidStrike.bluetoothchatapp;

import android.text.format.Time;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "sent_messages")
public class SentMessages {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "message_format")
    private String message;

    @ColumnInfo(name = "message")
    private char messageSent;

    @ColumnInfo(name = "sent_to_uid")
    private int sentToUid;

    @ColumnInfo(name = "sent_to_chat_name")
    private String sentToChatName;

    @ColumnInfo(name = "date_sent")
    private Date dateSent;

    @ColumnInfo(name = "time_sent")
    private Time timeSent;

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

    public char getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(char messageSent) {
        this.messageSent = messageSent;
    }

    public int getSentToUid() {
        return sentToUid;
    }

    public void setSentToUid(int sentToUid) {
        this.sentToUid = sentToUid;
    }

    public String getSentToChatName() {
        return sentToChatName;
    }

    public void setSentToChatName(String sentToChatName) {
        this.sentToChatName = sentToChatName;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public Time getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Time timeSent) {
        this.timeSent = timeSent;
    }
}

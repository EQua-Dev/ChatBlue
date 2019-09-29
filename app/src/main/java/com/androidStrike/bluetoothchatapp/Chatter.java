package com.androidStrike.bluetoothchatapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "chatter_detail")
public class Chatter {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "device_address")
    private int deviceAddress;

    @ColumnInfo(name = "device_name")
    private char deviceName;

    @ColumnInfo(name = "chatter_user_name")
    private String chatterUserName;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(int deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public char getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(char deviceName) {
        this.deviceName = deviceName;
    }

    public String getChatterUserName() {
        return chatterUserName;
    }

    public void setChatterUserName(String chatterUserName) {
        this.chatterUserName = chatterUserName;
    }
}

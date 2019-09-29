package com.androidStrike.bluetoothchatapp;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ChatterDao {

    @Insert
    public void addUser(Chatter user);
}

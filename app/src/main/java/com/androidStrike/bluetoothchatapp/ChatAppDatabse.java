package com.androidStrike.bluetoothchatapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Chatter.class,ReceivedMessages.class,SentMessages.class}, version = 1)
public abstract class ChatAppDatabse extends RoomDatabase {

    public abstract ChatterDao chatterDao();
    public abstract ReceivedMessagesDao receivedMessagesDao();
    public abstract SentMessagesDao sentMessagesDao();
}

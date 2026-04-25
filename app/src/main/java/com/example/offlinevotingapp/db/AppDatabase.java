package com.example.offlinevotingapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.offlinevotingapp.models.MyVoteEntity;
import com.example.offlinevotingapp.models.OptionEntity;
import com.example.offlinevotingapp.models.PollEntity;
import com.example.offlinevotingapp.models.VoteEntity;

@Database(entities = {PollEntity.class, OptionEntity.class, VoteEntity.class, MyVoteEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract PollDao pollDao();
    public abstract OptionDao optionDao();
    public abstract VoteDao voteDao();
    public abstract MyVoteDao myVoteDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "offline_voting_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

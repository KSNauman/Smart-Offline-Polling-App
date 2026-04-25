package com.example.offlinevotingapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.offlinevotingapp.models.PollEntity;

import java.util.List;

@Dao
public interface PollDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPoll(PollEntity poll);

    @Query("SELECT * FROM polls")
    List<PollEntity> getAllPolls();

    @Query("SELECT * FROM polls WHERE pollId = :pollId")
    PollEntity getPollById(String pollId);

    @Query("DELETE FROM polls WHERE pollId = :pollId")
    void deletePoll(String pollId);
}

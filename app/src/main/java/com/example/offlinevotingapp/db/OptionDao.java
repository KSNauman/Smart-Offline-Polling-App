package com.example.offlinevotingapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.offlinevotingapp.models.OptionEntity;

import java.util.List;

@Dao
public interface OptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOptions(List<OptionEntity> options);

    @Query("SELECT * FROM options WHERE pollId = :pollId")
    List<OptionEntity> getOptionsByPoll(String pollId);

    @Query("UPDATE options SET voteCount = voteCount + 1 WHERE optionId = :optionId")
    void incrementVoteCount(String optionId);
}

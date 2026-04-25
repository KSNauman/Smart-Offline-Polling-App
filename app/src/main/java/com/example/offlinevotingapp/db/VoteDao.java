package com.example.offlinevotingapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.offlinevotingapp.models.VoteEntity;

import java.util.List;

@Dao
public interface VoteDao {
    @Insert
    void insertVote(VoteEntity vote);

    @Query("SELECT EXISTS(SELECT 1 FROM votes WHERE pollId = :pollId AND deviceId = :deviceId)")
    boolean checkVoteExists(String pollId, String deviceId);

    @Query("SELECT * FROM votes WHERE pollId = :pollId")
    List<VoteEntity> getVotesByPoll(String pollId);
}

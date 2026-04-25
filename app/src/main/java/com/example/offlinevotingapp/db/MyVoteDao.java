package com.example.offlinevotingapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.offlinevotingapp.models.MyVoteEntity;

@Dao
public interface MyVoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMyVote(MyVoteEntity vote);

    @Query("SELECT * FROM my_votes WHERE pollId = :pollId LIMIT 1")
    MyVoteEntity getMyVote(String pollId);

    @Query("DELETE FROM my_votes WHERE pollId = :pollId")
    void deleteMyVote(String pollId);
}

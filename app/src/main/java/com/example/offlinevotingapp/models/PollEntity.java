package com.example.offlinevotingapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "polls")
public class PollEntity {
    @PrimaryKey
    @NonNull
    private String pollId;
    private String question;

    public PollEntity(@NonNull String pollId, String question) {
        this.pollId = pollId;
        this.question = question;
    }

    @NonNull
    public String getPollId() {
        return pollId;
    }

    public void setPollId(@NonNull String pollId) {
        this.pollId = pollId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

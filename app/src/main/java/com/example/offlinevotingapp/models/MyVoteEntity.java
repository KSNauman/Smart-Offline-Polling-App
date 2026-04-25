package com.example.offlinevotingapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_votes")
public class MyVoteEntity {
    @PrimaryKey
    @NonNull
    private String pollId;
    private String optionId;
    private String optionText;

    public MyVoteEntity(@NonNull String pollId, String optionId, String optionText) {
        this.pollId = pollId;
        this.optionId = optionId;
        this.optionText = optionText;
    }

    @NonNull
    public String getPollId() { return pollId; }
    public void setPollId(@NonNull String pollId) { this.pollId = pollId; }

    public String getOptionId() { return optionId; }
    public void setOptionId(String optionId) { this.optionId = optionId; }

    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }
}

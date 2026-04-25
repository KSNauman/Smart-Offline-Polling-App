package com.example.offlinevotingapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "options",
    foreignKeys = @ForeignKey(
        entity = PollEntity.class,
        parentColumns = "pollId",
        childColumns = "pollId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("pollId")}
)
public class OptionEntity {
    @PrimaryKey
    @NonNull
    private String optionId;
    private String pollId;
    private String text;
    private int voteCount;

    public OptionEntity(@NonNull String optionId, String pollId, String text, int voteCount) {
        this.optionId = optionId;
        this.pollId = pollId;
        this.text = text;
        this.voteCount = voteCount;
    }

    @NonNull
    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(@NonNull String optionId) {
        this.optionId = optionId;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}

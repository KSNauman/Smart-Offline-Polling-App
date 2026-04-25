package com.example.offlinevotingapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "votes")
public class VoteEntity {
    @PrimaryKey(autoGenerate = true)
    private int voteId;
    private String pollId;
    private String optionId;
    private String deviceId;
    private String userName;

    public VoteEntity(String pollId, String optionId, String deviceId, String userName) {
        this.pollId = pollId;
        this.optionId = optionId;
        this.deviceId = deviceId;
        this.userName = userName;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

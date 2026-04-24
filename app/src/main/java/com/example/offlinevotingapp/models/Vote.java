package com.example.offlinevotingapp.models;

/**
 * Represents a single vote cast by a device for a specific option in a poll.
 * Phase 1 — skeleton model, no persistence logic yet.
 */
public class Vote {

    private String pollId;
    private String optionId;
    private String deviceId;

    public Vote() {
        // Default constructor
    }

    public Vote(String pollId, String optionId, String deviceId) {
        this.pollId = pollId;
        this.optionId = optionId;
        this.deviceId = deviceId;
    }

    // --- Getters ---

    public String getPollId() {
        return pollId;
    }

    public String getOptionId() {
        return optionId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    // --- Setters ---

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "Vote{pollId='" + pollId + "', optionId='" + optionId + "', deviceId='" + deviceId + "'}";
    }
}

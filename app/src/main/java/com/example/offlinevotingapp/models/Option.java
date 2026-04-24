package com.example.offlinevotingapp.models;

/**
 * Represents a single voting option within a poll.
 * Phase 1 — skeleton model, no persistence logic yet.
 */
public class Option {

    private String optionId;
    private String text;
    private int voteCount;

    public Option() {
        // Default constructor
    }

    public Option(String optionId, String text) {
        this.optionId = optionId;
        this.text = text;
        this.voteCount = 0;
    }

    public Option(String optionId, String text, int voteCount) {
        this.optionId = optionId;
        this.text = text;
        this.voteCount = voteCount;
    }

    // --- Getters ---

    public String getOptionId() {
        return optionId;
    }

    public String getText() {
        return text;
    }

    public int getVoteCount() {
        return voteCount;
    }

    // --- Setters ---

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "Option{optionId='" + optionId + "', text='" + text + "', voteCount=" + voteCount + "}";
    }
}

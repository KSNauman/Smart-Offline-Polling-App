package com.example.offlinevotingapp.models;

import java.util.List;

/**
 * Represents a poll with a question and a list of options.
 * Phase 1 — skeleton model, no persistence logic yet.
 */
public class Poll {

    private String pollId;
    private String question;
    private List<Option> options;

    public Poll() {
        // Default constructor required for future serialization
    }

    public Poll(String pollId, String question, List<Option> options) {
        this.pollId = pollId;
        this.question = question;
        this.options = options;
    }

    // --- Getters ---

    public String getPollId() {
        return pollId;
    }

    public String getQuestion() {
        return question;
    }

    public List<Option> getOptions() {
        return options;
    }

    // --- Setters ---

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Poll{pollId='" + pollId + "', question='" + question + "', options=" + options + "}";
    }
}

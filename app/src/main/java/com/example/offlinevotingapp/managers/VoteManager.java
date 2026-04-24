package com.example.offlinevotingapp.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Singleton class for in-memory storage of votes.
 * Phase 5 implementation.
 */
public class VoteManager {

    private static VoteManager instance;

    // key = option_id, value = vote count
    private final Map<String, Integer> voteCountMap;

    // device IDs that have already voted to prevent duplicates
    private final Set<String> votedDevices;

    private VoteManager() {
        voteCountMap = new HashMap<>();
        votedDevices = new HashSet<>();
    }

    public static synchronized VoteManager getInstance() {
        if (instance == null) {
            instance = new VoteManager();
        }
        return instance;
    }

    /**
     * Checks if a device has already voted.
     */
    public boolean hasVoted(String deviceId) {
        return votedDevices.contains(deviceId);
    }

    /**
     * Adds a vote to the system if the device hasn't voted yet.
     */
    public void addVote(String optionId, String deviceId) {
        if (!hasVoted(deviceId)) {
            votedDevices.add(deviceId);
            int currentCount = voteCountMap.getOrDefault(optionId, 0);
            voteCountMap.put(optionId, currentCount + 1);
        }
    }

    /**
     * Returns a copy of the results.
     */
    public Map<String, Integer> getResults() {
        return new HashMap<>(voteCountMap);
    }

    /**
     * Clears all votes (useful for testing or starting a new poll).
     */
    public void clearAll() {
        voteCountMap.clear();
        votedDevices.clear();
    }
}

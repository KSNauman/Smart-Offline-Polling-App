package com.example.offlinevotingapp.utils;

import com.example.offlinevotingapp.models.Option;
import com.example.offlinevotingapp.models.Poll;
import com.example.offlinevotingapp.models.Vote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for converting between Poll objects and JSON strings.
 *
 * Phase 3 additions:
 *  - parsePollFromJson(String json)  — deserializes a JSON string → Poll object
 *
 * The expected JSON format is:
 * <pre>
 * {
 *   "poll_id":  "uuid-string",
 *   "question": "Your question text",
 *   "options": [
 *     { "option_id": "1", "text": "Option A" },
 *     { "option_id": "2", "text": "Option B" }
 *   ]
 * }
 * </pre>
 */
public class JsonUtils {

    // Private constructor — static utility class
    private JsonUtils() {}

    // -------------------------------------------------------------------------
    // Deserialization  (JSON → Poll)
    // -------------------------------------------------------------------------

    /**
     * Parses a JSON string into a {@link Poll} object.
     *
     * <p>Returns {@code null} if:
     * <ul>
     *   <li>The input is null or empty</li>
     *   <li>The JSON is malformed</li>
     *   <li>Required fields (poll_id, question, options) are missing</li>
     * </ul>
     *
     * @param json The JSON string to parse (typically from a scanned QR code).
     * @return A fully-populated {@link Poll}, or {@code null} on any error.
     */
    public static Poll parsePollFromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            JSONObject pollJson = new JSONObject(json);

            // --- Required top-level fields ---
            String pollId   = pollJson.getString("poll_id");
            String question = pollJson.getString("question");

            // --- Options array ---
            JSONArray optionsArray = pollJson.getJSONArray("options");
            List<Option> options = new ArrayList<>();

            for (int i = 0; i < optionsArray.length(); i++) {
                JSONObject optionJson = optionsArray.getJSONObject(i);
                String optionId = optionJson.getString("option_id");
                String text     = optionJson.getString("text");
                options.add(new Option(optionId, text));
            }

            return new Poll(pollId, question, options);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Serialization  (Poll → JSON)
    // -------------------------------------------------------------------------

    /**
     * Converts a {@link Poll} object into its JSON string representation.
     *
     * <p>This mirrors the logic in {@code CreatePollActivity.convertPollToJson()},
     * centralised here for reuse by any future component that needs serialization.
     *
     * @param poll The poll to serialize.
     * @return JSON string, or {@code null} if serialization fails.
     */
    public static String convertPollToJson(Poll poll) {
        if (poll == null) {
            return null;
        }

        try {
            JSONObject pollJson = new JSONObject();
            pollJson.put("poll_id",  poll.getPollId());
            pollJson.put("question", poll.getQuestion());

            JSONArray optionsArray = new JSONArray();
            for (Option option : poll.getOptions()) {
                JSONObject optionJson = new JSONObject();
                optionJson.put("option_id", option.getOptionId());
                optionJson.put("text",      option.getText());
                optionsArray.put(optionJson);
            }

            pollJson.put("options", optionsArray);
            return pollJson.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a {@link Vote} object into its JSON string representation.
     *
     * @param vote The vote to serialize.
     * @return JSON string, or {@code null} if serialization fails.
     */
    public static String convertVoteToJson(Vote vote) {
        if (vote == null) return null;

        try {
            JSONObject voteJson = new JSONObject();
            voteJson.put("poll_id",   vote.getPollId());
            voteJson.put("option_id", vote.getOptionId());
            voteJson.put("device_id", vote.getDeviceId());
            
            return voteJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses a JSON string into a {@link Vote} object.
     *
     * @param json The JSON string to parse.
     * @return A {@link Vote} object, or {@code null} on error.
     */
    public static Vote parseVoteFromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            JSONObject voteJson = new JSONObject(json);
            String pollId = voteJson.getString("poll_id");
            String optionId = voteJson.getString("option_id");
            String deviceId = voteJson.getString("device_id");

            return new Vote(pollId, optionId, deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

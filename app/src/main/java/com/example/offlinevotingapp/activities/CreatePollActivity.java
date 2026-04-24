package com.example.offlinevotingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityCreatePollBinding;
import com.example.offlinevotingapp.models.Option;
import com.example.offlinevotingapp.models.Poll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * CreatePollActivity — Allows the user to compose a poll.
 *
 * Responsibilities (Phase 2):
 *  - Read poll question from EditText
 *  - Dynamically add option input fields (2–4 options minimum)
 *  - Build a Poll object with a UUID poll_id and List<Option>
 *  - Convert Poll → JSON string via convertPollToJson()
 *  - Pass JSON to ShowQRActivity via Intent
 *  - Validate: question non-empty, at least 2 options filled
 */
public class CreatePollActivity extends AppCompatActivity {

    private ActivityCreatePollBinding binding;

    /** Counter tracking how many option fields have been added. */
    private int optionCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreatePollBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    // -------------------------------------------------------------------------
    // Listener Setup
    // -------------------------------------------------------------------------

    private void setupListeners() {

        // Dynamically add a new option EditText to the options container
        binding.btnAddOption.setOnClickListener(v -> {
            optionCount++;
            addOptionField(optionCount);
        });

        // Validate inputs → build Poll → serialize → hand off to ShowQRActivity
        binding.btnGenerateQr.setOnClickListener(v -> onGenerateQrClicked());
    }

    // -------------------------------------------------------------------------
    // Generate QR Button Handler
    // -------------------------------------------------------------------------

    private void onGenerateQrClicked() {
        String question = binding.etQuestion.getText().toString().trim();

        // --- Validation ---
        if (TextUtils.isEmpty(question)) {
            Toast.makeText(this, "Please enter a poll question.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Option> options = collectOptions();

        if (options.size() < 2) {
            Toast.makeText(this, "Please add at least 2 options.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Build Poll object ---
        String pollId = UUID.randomUUID().toString();
        Poll poll = new Poll(pollId, question, options);

        // --- Serialize to JSON ---
        String jsonString = convertPollToJson(poll);

        if (jsonString == null) {
            Toast.makeText(this, "Error creating poll JSON. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Navigate to ShowQRActivity ---
        Intent intent = new Intent(CreatePollActivity.this, ShowQRActivity.class);
        intent.putExtra(AppConstants.EXTRA_POLL_JSON, jsonString);
        startActivity(intent);
    }

    // -------------------------------------------------------------------------
    // Option Collection
    // -------------------------------------------------------------------------

    /**
     * Iterates through all child views in llOptionsContainer, reads their text,
     * and returns only those with non-empty text as Option objects.
     *
     * @return List of filled-in Option objects (may be empty if none filled).
     */
    private List<Option> collectOptions() {
        List<Option> options = new ArrayList<>();
        LinearLayout container = binding.llOptionsContainer;
        int childCount = container.getChildCount();

        for (int i = 0; i < childCount; i++) {
            android.view.View child = container.getChildAt(i);
            if (child instanceof EditText) {
                String text = ((EditText) child).getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    // option_id is 1-based, matching the visual index
                    String optionId = String.valueOf(i + 1);
                    options.add(new Option(optionId, text));
                }
            }
        }

        return options;
    }

    // -------------------------------------------------------------------------
    // JSON Conversion
    // -------------------------------------------------------------------------

    /**
     * Converts a Poll object into a JSON string with the following structure:
     * <pre>
     * {
     *   "poll_id":  "...",
     *   "question": "...",
     *   "options": [
     *     { "option_id": "1", "text": "..." },
     *     { "option_id": "2", "text": "..." }
     *   ]
     * }
     * </pre>
     *
     * @param poll The Poll to serialize.
     * @return JSON string, or null if serialization fails.
     */
    public String convertPollToJson(Poll poll) {
        try {
            JSONObject pollJson = new JSONObject();
            pollJson.put("poll_id", poll.getPollId());
            pollJson.put("question", poll.getQuestion());

            JSONArray optionsArray = new JSONArray();
            for (Option option : poll.getOptions()) {
                JSONObject optionJson = new JSONObject();
                optionJson.put("option_id", option.getOptionId());
                optionJson.put("text", option.getText());
                optionsArray.put(optionJson);
            }

            pollJson.put("options", optionsArray);
            return pollJson.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Dynamic Option Field Creation
    // -------------------------------------------------------------------------

    /**
     * Programmatically inflates and adds a new EditText for a poll option.
     *
     * @param index The 1-based index of the option (used for the hint text).
     */
    private void addOptionField(int index) {
        EditText optionField = new EditText(this);
        optionField.setHint("Option " + index);
        optionField.setId(android.view.View.generateViewId());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = (int) (8 * getResources().getDisplayMetrics().density);
        optionField.setLayoutParams(params);

        binding.llOptionsContainer.addView(optionField);
    }
}

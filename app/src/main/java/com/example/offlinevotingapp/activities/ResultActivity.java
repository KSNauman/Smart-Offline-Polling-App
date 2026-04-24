package com.example.offlinevotingapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.databinding.ActivityResultBinding;
import com.example.offlinevotingapp.managers.VoteManager;

import java.util.Map;

/**
 * ResultActivity — Displays the voting results for a poll.
 *
 * Responsibilities (Phase 5):
 *  - Get voteCountMap from VoteManager
 *  - Display Option text and vote count
 */
public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        displayResults();
    }

    private void displayResults() {
        VoteManager voteManager = VoteManager.getInstance();
        Map<String, Integer> results = voteManager.getResults();

        if (results.isEmpty()) {
            binding.tvResults.setText("No votes received yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        int totalVotes = 0;
        
        for (Integer count : results.values()) {
            totalVotes += count;
        }

        sb.append("Total Votes: ").append(totalVotes).append("\n\n");

        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            String optionId = entry.getKey();
            int count = entry.getValue();
            
            // In a full implementation, we'd look up the option text using the optionId from the original Poll.
            // For simplicity in this phase, we display "Option X -> Y votes".
            sb.append("Option ").append(optionId)
              .append(" \u2192 ")
              .append(count)
              .append(" votes");

            if (totalVotes > 0) {
                int percentage = (int) (((float) count / totalVotes) * 100);
                sb.append(" (").append(percentage).append("%)");
            }
            sb.append("\n\n");
        }

        binding.tvResults.setText(sb.toString().trim());
        binding.tvResultTitle.setText("Live Poll Results");
    }
}

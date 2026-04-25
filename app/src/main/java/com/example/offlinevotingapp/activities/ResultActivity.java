package com.example.offlinevotingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.R;
import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityResultBinding;
import com.example.offlinevotingapp.managers.UserManager;
import com.example.offlinevotingapp.models.MyVoteEntity;
import com.example.offlinevotingapp.models.Option;
import com.example.offlinevotingapp.models.OptionEntity;
import com.example.offlinevotingapp.models.Poll;
import com.example.offlinevotingapp.models.Vote;
import com.example.offlinevotingapp.repository.VotingRepository;
import com.example.offlinevotingapp.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    private VotingRepository repository;
    private UserManager userManager;
    private String pollId;
    private String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new VotingRepository(this);
        userManager = new UserManager(this);
        pollId = getIntent().getStringExtra("POLL_ID");
        question = getIntent().getStringExtra("QUESTION");

        binding.tvPollQuestion.setText(question);
        binding.btnBack.setOnClickListener(v -> finish());

        loadResults();
        checkMyVote();
        setupListeners();
    }

    private void loadResults() {
        repository.getOptionsByPoll(pollId, options -> {
            runOnUiThread(() -> populateResults(options));
        });
    }

    private void checkMyVote() {
        repository.getMyVote(pollId, myVote -> {
            if (myVote != null) {
                runOnUiThread(() -> {
                    binding.btnReshowMyVote.setVisibility(View.VISIBLE);
                    binding.btnReshowMyVote.setOnClickListener(v -> showMyVoteQr(myVote));
                });
            }
        });
    }

    private void setupListeners() {
        binding.btnReshowPollQr.setOnClickListener(v -> reshowPollQr());
    }

    private void populateResults(List<OptionEntity> options) {
        binding.llResultsContainer.removeAllViews();
        
        int totalVotes = 0;
        int maxVotes = -1;
        OptionEntity winner = null;

        for (OptionEntity opt : options) {
            totalVotes += opt.getVoteCount();
            if (opt.getVoteCount() > maxVotes) {
                maxVotes = opt.getVoteCount();
                winner = opt;
            } else if (opt.getVoteCount() == maxVotes) {
                winner = null; // Tie
            }
        }

        for (OptionEntity opt : options) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_result_option, binding.llResultsContainer, false);
            TextView tvName = view.findViewById(R.id.tvOptionName);
            TextView tvCount = view.findViewById(R.id.tvVoteCount);
            TextView tvPercent = view.findViewById(R.id.tvPercentage);
            ProgressBar pb = view.findViewById(R.id.pbVotes);

            tvName.setText(opt.getText());
            tvCount.setText(opt.getVoteCount() + " votes");
            
            int percent = (totalVotes > 0) ? (opt.getVoteCount() * 100 / totalVotes) : 0;
            tvPercent.setText(percent + "%");
            pb.setProgress(percent);

            if (winner != null && opt.getOptionId().equals(winner.getOptionId())) {
                tvName.setTextColor(getResources().getColor(R.color.primary_blue));
                tvName.setText("👑 " + opt.getText());
            }

            binding.llResultsContainer.addView(view);
        }
        
        binding.tvTotalVotes.setText("Total Votes: " + totalVotes);
    }

    private void showMyVoteQr(MyVoteEntity myVote) {
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String userName = userManager.getUserName();
        Vote vote = new Vote(myVote.getPollId(), myVote.getOptionId(), deviceId, userName);
        String json = JsonUtils.convertVoteToJson(vote);

        Intent intent = new Intent(this, ShowQRActivity.class);
        intent.putExtra(AppConstants.EXTRA_POLL_JSON, json);
        intent.putExtra(AppConstants.EXTRA_QR_SCREEN_TITLE, "My Vote QR");
        intent.putExtra(AppConstants.EXTRA_QR_HINT, "Share this QR with the host to cast your vote.");
        startActivity(intent);
    }

    private void reshowPollQr() {
        repository.getOptionsByPoll(pollId, options -> {
            List<Option> pollOptions = new ArrayList<>();
            for (OptionEntity opt : options) {
                pollOptions.add(new Option(opt.getOptionId(), opt.getText()));
            }
            Poll poll = new Poll(pollId, question, pollOptions);
            String json = JsonUtils.convertPollToJson(poll);

            runOnUiThread(() -> {
                Intent intent = new Intent(this, ShowQRActivity.class);
                intent.putExtra(AppConstants.EXTRA_POLL_JSON, json);
                intent.putExtra(AppConstants.EXTRA_QR_SCREEN_TITLE, "Poll QR");
                intent.putExtra(AppConstants.EXTRA_QR_HINT, "Voters should scan this QR to participate.");
                startActivity(intent);
            });
        });
    }
}

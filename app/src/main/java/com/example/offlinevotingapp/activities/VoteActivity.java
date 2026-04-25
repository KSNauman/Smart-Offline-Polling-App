package com.example.offlinevotingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.R;
import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityVoteBinding;
import com.example.offlinevotingapp.managers.UserManager;
import com.example.offlinevotingapp.models.MyVoteEntity;
import com.example.offlinevotingapp.models.Option;
import com.example.offlinevotingapp.models.Poll;
import com.example.offlinevotingapp.models.Vote;
import com.example.offlinevotingapp.repository.VotingRepository;
import com.example.offlinevotingapp.utils.JsonUtils;

public class VoteActivity extends AppCompatActivity {

    private ActivityVoteBinding binding;
    private Poll currentPoll;
    private UserManager userManager;
    private VotingRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userManager = new UserManager(this);
        repository = new VotingRepository(this);
        loadPollFromIntent();
        setupListeners();
    }

    private void loadPollFromIntent() {
        String pollJson = getIntent().getStringExtra(AppConstants.EXTRA_POLL_JSON);
        if (pollJson == null || pollJson.isEmpty()) {
            Toast.makeText(this, "No poll data found.", Toast.LENGTH_LONG).show();
            finish(); return;
        }
        currentPoll = JsonUtils.parsePollFromJson(pollJson);
        if (currentPoll == null) {
            Toast.makeText(this, "Invalid poll QR.", Toast.LENGTH_LONG).show();
            finish(); return;
        }
        populatePollUi(currentPoll);
    }

    private void populatePollUi(Poll poll) {
        binding.tvPollQuestion.setText(poll.getQuestion());
        binding.rgVoteOptions.removeAllViews();
        
        for (Option opt : poll.getOptions()) {
            RadioButton rb = (RadioButton) LayoutInflater.from(this).inflate(R.layout.item_vote_option, binding.rgVoteOptions, false);
            rb.setText(opt.getText());
            rb.setTag(opt.getOptionId());
            binding.rgVoteOptions.addView(rb);
        }
    }

    private void setupListeners() {
        binding.btnSubmitVote.setOnClickListener(v -> onSubmitVote());
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void onSubmitVote() {
        int selectedId = binding.rgVoteOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an option.", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRb = findViewById(selectedId);
        String optionId = (String) selectedRb.getTag();
        String optionText = selectedRb.getText().toString();
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String userName = userManager.getUserName();

        // Save locally for re-sharing
        repository.saveMyVote(new MyVoteEntity(currentPoll.getPollId(), optionId, optionText));

        Vote vote = new Vote(currentPoll.getPollId(), optionId, deviceId, userName);
        String voteJson = JsonUtils.convertVoteToJson(vote);

        if (voteJson == null) {
            Toast.makeText(this, "Error creating vote data.", Toast.LENGTH_SHORT).show();
            return;
        }

        openVoteQrScreen(voteJson);
    }

    private void openVoteQrScreen(String voteJson) {
        Intent intent = new Intent(this, ShowQRActivity.class);
        intent.putExtra(AppConstants.EXTRA_POLL_JSON, voteJson);
        intent.putExtra(AppConstants.EXTRA_QR_SCREEN_TITLE, "Vote QR");
        intent.putExtra(AppConstants.EXTRA_QR_HINT, "The host should scan this QR to receive your vote.");
        startActivity(intent);
        finish(); // Close voting screen once QR is shown
    }
}

package com.example.offlinevotingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.Locale;

public class VoteActivity extends AppCompatActivity {

    private ActivityVoteBinding binding;
    private Poll currentPoll;
    private UserManager userManager;
    private VotingRepository repository;
    private TextToSpeech textToSpeech;
    private boolean isTtsReady = false;

    // Track the currently selected option
    private View selectedOptionView = null;
    private String selectedOptionId = null;
    private String selectedOptionText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userManager = new UserManager(this);
        repository = new VotingRepository(this);

        initTextToSpeech();
        loadPollFromIntent();
        setupListeners();
    }

    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result != TextToSpeech.LANG_MISSING_DATA
                        && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTtsReady = true;
                }
            }
        });
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
        binding.llVoteOptions.removeAllViews();

        for (Option opt : poll.getOptions()) {
            View itemView = LayoutInflater.from(this)
                    .inflate(R.layout.item_vote_option, binding.llVoteOptions, false);

            TextView tvOption = itemView.findViewById(R.id.tvOptionText);
            ImageButton btnSpeak = itemView.findViewById(R.id.btnSpeak);

            tvOption.setText(opt.getText());
            itemView.setTag(opt.getOptionId());

            // Handle option selection (tap on the row to select)
            itemView.setOnClickListener(v -> selectOption(v, opt));

            // Handle speaker button (speak this single option)
            btnSpeak.setOnClickListener(v -> speakSingleOption(opt.getText()));

            binding.llVoteOptions.addView(itemView);
        }
    }

    private void selectOption(View optionView, Option opt) {
        // Deselect previously selected option
        if (selectedOptionView != null) {
            selectedOptionView.setSelected(false);
        }

        // Select the new option
        optionView.setSelected(true);
        selectedOptionView = optionView;
        selectedOptionId = opt.getOptionId();
        selectedOptionText = opt.getText();
    }

    private void speakSingleOption(String optionText) {
        if (!isTtsReady) {
            Toast.makeText(this, "Text-to-Speech is not ready yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        // If already speaking, stop first
        if (textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }

        textToSpeech.speak(optionText, TextToSpeech.QUEUE_FLUSH, null, "option_speak");
    }

    private void setupListeners() {
        binding.btnSubmitVote.setOnClickListener(v -> onSubmitVote());
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void onSubmitVote() {
        if (selectedOptionId == null) {
            Toast.makeText(this, "Please select an option.", Toast.LENGTH_SHORT).show();
            return;
        }

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String userName = userManager.getUserName();

        // Save locally for re-sharing
        repository.saveMyVote(new MyVoteEntity(currentPoll.getPollId(), selectedOptionId, selectedOptionText));

        Vote vote = new Vote(currentPoll.getPollId(), selectedOptionId, deviceId, userName);
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
        finish();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}

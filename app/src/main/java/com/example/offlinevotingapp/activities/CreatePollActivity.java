package com.example.offlinevotingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.R;
import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityCreatePollBinding;
import com.example.offlinevotingapp.models.Option;
import com.example.offlinevotingapp.models.OptionEntity;
import com.example.offlinevotingapp.models.Poll;
import com.example.offlinevotingapp.models.PollEntity;
import com.example.offlinevotingapp.repository.VotingRepository;
import com.example.offlinevotingapp.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreatePollActivity extends AppCompatActivity {

    private ActivityCreatePollBinding binding;
    private VotingRepository repository;
    private final List<EditText> optionEditTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePollBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new VotingRepository(this);

        setupListeners();
        // Add initial two options
        addOptionField();
        addOptionField();
    }

    private void setupListeners() {
        binding.btnAddOption.setOnClickListener(v -> addOptionField());
        binding.btnGenerateQr.setOnClickListener(v -> generatePoll());
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void addOptionField() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_option_input, binding.llOptionsContainer, false);
        EditText etOption = view.findViewById(R.id.etOption);
        ImageButton btnRemove = view.findViewById(R.id.btnRemoveOption);

        optionEditTexts.add(etOption);
        binding.llOptionsContainer.addView(view);

        btnRemove.setOnClickListener(v -> {
            if (optionEditTexts.size() > 2) {
                optionEditTexts.remove(etOption);
                binding.llOptionsContainer.removeView(view);
            } else {
                Toast.makeText(this, "Minimum 2 options required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generatePoll() {
        String question = binding.etQuestion.getText().toString().trim();
        if (question.isEmpty()) {
            Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Option> options = new ArrayList<>();
        List<OptionEntity> optionEntities = new ArrayList<>();
        String pollId = UUID.randomUUID().toString();

        for (int i = 0; i < optionEditTexts.size(); i++) {
            String text = optionEditTexts.get(i).getText().toString().trim();
            if (!text.isEmpty()) {
                String optionId = UUID.randomUUID().toString();
                options.add(new Option(optionId, text));
                optionEntities.add(new OptionEntity(optionId, pollId, text, 0));
            }
        }

        if (options.size() < 2) {
            Toast.makeText(this, "Please add at least 2 options", Toast.LENGTH_SHORT).show();
            return;
        }

        Poll poll = new Poll(pollId, question, options);
        PollEntity pollEntity = new PollEntity(pollId, question);

        repository.createPoll(pollEntity, optionEntities, () -> {
            runOnUiThread(() -> {
                String json = JsonUtils.convertPollToJson(poll);
                Intent intent = new Intent(this, ShowQRActivity.class);
                intent.putExtra(AppConstants.EXTRA_POLL_JSON, json);
                intent.putExtra(AppConstants.EXTRA_QR_SCREEN_TITLE, "Poll QR");
                intent.putExtra(AppConstants.EXTRA_QR_HINT, "Voters should scan this QR to participate.");
                startActivity(intent);
                finish();
            });
        });
    }
}

package com.example.offlinevotingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.offlinevotingapp.adapters.PollAdapter;
import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityMainBinding;
import com.example.offlinevotingapp.managers.UserManager;
import com.example.offlinevotingapp.models.Option;
import com.example.offlinevotingapp.models.Poll;
import com.example.offlinevotingapp.models.PollEntity;
import com.example.offlinevotingapp.repository.VotingRepository;
import com.example.offlinevotingapp.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VotingRepository repository;
    private PollAdapter adapter;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        userManager = new UserManager(this);
        if (!userManager.isRegistered()) {
            startActivity(new Intent(this, RegistrationActivity.class));
            finish();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new VotingRepository(this);
        setupRecyclerView();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPolls();
    }

    private void setupRecyclerView() {
        adapter = new PollAdapter(new PollAdapter.OnPollClickListener() {
            @Override
            public void onPollClick(PollEntity poll) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("POLL_ID", poll.getPollId());
                intent.putExtra("QUESTION", poll.getQuestion());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(PollEntity poll) {
                repository.deletePoll(poll.getPollId(), () -> runOnUiThread(() -> loadPolls()));
            }

            @Override
            public void onShareClick(PollEntity poll) {
                sharePollQr(poll);
            }
        });
        binding.rvPolls.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPolls.setAdapter(adapter);
    }

    private void sharePollQr(PollEntity pollEntity) {
        repository.getOptionsByPoll(pollEntity.getPollId(), options -> {
            List<Option> pollOptions = new ArrayList<>();
            for (com.example.offlinevotingapp.models.OptionEntity opt : options) {
                pollOptions.add(new Option(opt.getOptionId(), opt.getText()));
            }
            Poll poll = new Poll(pollEntity.getPollId(), pollEntity.getQuestion(), pollOptions);
            String json = JsonUtils.convertPollToJson(poll);
            
            runOnUiThread(() -> {
                Intent intent = new Intent(MainActivity.this, ShowQRActivity.class);
                intent.putExtra(AppConstants.EXTRA_POLL_JSON, json);
                intent.putExtra(AppConstants.EXTRA_QR_SCREEN_TITLE, "Poll QR");
                intent.putExtra(AppConstants.EXTRA_QR_HINT, "Voters should scan this QR to participate.");
                startActivity(intent);
            });
        });
    }

    private void loadPolls() {
        repository.getAllPolls(polls -> {
            runOnUiThread(() -> {
                if (polls.isEmpty()) {
                    binding.tvEmptyState.setVisibility(View.VISIBLE);
                    binding.rvPolls.setVisibility(View.GONE);
                } else {
                    binding.tvEmptyState.setVisibility(View.GONE);
                    binding.rvPolls.setVisibility(View.VISIBLE);
                    adapter.setPolls(polls);
                }
            });
        });
    }

    private void setupListeners() {
        binding.btnCreatePoll.setOnClickListener(v -> {
            startActivity(new Intent(this, CreatePollActivity.class));
        });

        binding.btnScanPoll.setOnClickListener(v -> {
            startActivity(new Intent(this, ScanPollActivity.class));
        });

        binding.btnScanVote.setOnClickListener(v -> {
            startActivity(new Intent(this, ScanVoteActivity.class));
        });
    }
}

package com.example.offlinevotingapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityScanVoteBinding;
import com.example.offlinevotingapp.models.Vote;
import com.example.offlinevotingapp.models.VoteEntity;
import com.example.offlinevotingapp.repository.VotingRepository;
import com.example.offlinevotingapp.utils.JsonUtils;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * ScanVoteActivity — Host interface for receiving votes via QR.
 * NFC functionality removed.
 */
public class ScanVoteActivity extends AppCompatActivity {

    private ActivityScanVoteBinding binding;
    private VotingRepository repository;

    private final ActivityResultLauncher<ScanOptions> qrScanLauncher =
            registerForActivityResult(new ScanContract(), this::onQrScanResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanVoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new VotingRepository(this);
        setupListeners();
    }

    private void setupListeners() {
        binding.btnScanVoteQr.setOnClickListener(v -> launchQrScanner());
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void launchQrScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan Vote QR");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        qrScanLauncher.launch(options);
    }

    private void onQrScanResult(ScanIntentResult result) {
        if (result.getContents() != null) {
            processVoteJson(result.getContents());
        }
    }

    private void processVoteJson(String json) {
        Vote vote = JsonUtils.parseVoteFromJson(json);
        if (vote == null) {
            Toast.makeText(this, "Invalid vote data", Toast.LENGTH_SHORT).show();
            return;
        }

        VoteEntity entity = new VoteEntity(vote.getPollId(), vote.getOptionId(), vote.getDeviceId(), vote.getUserName());
        repository.castVote(entity, 
            () -> runOnUiThread(() -> {
                Toast.makeText(this, "Vote from " + vote.getUserName() + " received!", Toast.LENGTH_LONG).show();
                binding.tvScanResult.setText("Last Vote: " + vote.getUserName());
            }),
            () -> runOnUiThread(() -> {
                Toast.makeText(this, "Duplicate vote ignored", Toast.LENGTH_SHORT).show();
                binding.tvScanResult.setText("Duplicate: " + vote.getUserName());
            })
        );
    }
}

package com.example.offlinevotingapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.databinding.ActivityScanVoteBinding;
import com.example.offlinevotingapp.managers.VoteManager;
import com.example.offlinevotingapp.models.Vote;
import com.example.offlinevotingapp.utils.JsonUtils;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * ScanVoteActivity — Hosts the QR scan trigger UI for receiving votes.
 *
 * Responsibilities (Phase 5):
 *  - Launch the ZXing embedded QR scanner on button click
 *  - Handle the scan result via ActivityResultLauncher
 *  - Parse vote JSON and add to VoteManager
 *  - Prevent duplicate votes
 */
public class ScanVoteActivity extends AppCompatActivity {

    private ActivityScanVoteBinding binding;

    private final ActivityResultLauncher<ScanOptions> qrScanLauncher =
            registerForActivityResult(new ScanContract(), this::onQrScanResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScanVoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    private void setupListeners() {
        binding.btnScanVoteQr.setOnClickListener(v -> launchQrScanner());
    }

    private void launchQrScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan Vote QR");
        options.setCameraId(0);
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setBarcodeImageEnabled(false);

        qrScanLauncher.launch(options);
    }

    private void onQrScanResult(ScanIntentResult result) {
        if (result.getContents() == null) {
            Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
            binding.tvScanResult.setText("No QR scanned yet");
            return;
        }

        String scannedText = result.getContents();

        Vote vote = JsonUtils.parseVoteFromJson(scannedText);
        if (vote == null) {
            Toast.makeText(this, "Invalid vote QR code", Toast.LENGTH_SHORT).show();
            binding.tvScanResult.setText("Invalid QR code");
            return;
        }

        VoteManager voteManager = VoteManager.getInstance();
        if (voteManager.hasVoted(vote.getDeviceId())) {
            Toast.makeText(this, "Duplicate vote ignored", Toast.LENGTH_SHORT).show();
            binding.tvScanResult.setText("Duplicate vote ignored");
        } else {
            voteManager.addVote(vote.getOptionId(), vote.getDeviceId());
            Toast.makeText(this, "Vote received!", Toast.LENGTH_SHORT).show();
            binding.tvScanResult.setText("Vote received successfully!");
        }
    }
}

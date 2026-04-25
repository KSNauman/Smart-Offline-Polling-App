package com.example.offlinevotingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityScanPollBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanPollActivity extends AppCompatActivity {

    private ActivityScanPollBinding binding;

    private final ActivityResultLauncher<ScanOptions> qrScanLauncher =
            registerForActivityResult(new ScanContract(), this::onQrScanResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanPollBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    private void setupListeners() {
        binding.btnScanQr.setOnClickListener(v -> launchQrScanner());
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void launchQrScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan Poll QR");
        options.setCameraId(0);
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        qrScanLauncher.launch(options);
    }

    private void onQrScanResult(ScanIntentResult result) {
        if (result.getContents() == null) {
            Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
            return;
        }

        String scannedText = result.getContents();
        Intent intent = new Intent(ScanPollActivity.this, VoteActivity.class);
        intent.putExtra(AppConstants.EXTRA_POLL_JSON, scannedText);
        startActivity(intent);
    }
}

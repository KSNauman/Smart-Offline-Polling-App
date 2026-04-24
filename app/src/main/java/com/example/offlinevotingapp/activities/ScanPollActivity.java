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

/**
 * ScanPollActivity — Hosts the QR scan trigger UI.
 *
 * Responsibilities (Phase 3):
 *  - Launch the ZXing embedded QR scanner on button click
 *  - Handle the scan result via ActivityResultLauncher
 *  - On success: pass the scanned JSON to VoteActivity via Intent
 *  - On cancel: show a Toast and reset the result TextView
 */
public class ScanPollActivity extends AppCompatActivity {

    private ActivityScanPollBinding binding;

    /**
     * ActivityResultLauncher for ZXing embedded scanner.
     * Must be registered before onCreate() per AndroidX Activity Result API rules.
     */
    private final ActivityResultLauncher<ScanOptions> qrScanLauncher =
            registerForActivityResult(new ScanContract(), this::onQrScanResult);

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScanPollBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    // -------------------------------------------------------------------------
    // Listener Setup
    // -------------------------------------------------------------------------

    private void setupListeners() {
        binding.btnScanQr.setOnClickListener(v -> launchQrScanner());
    }

    // -------------------------------------------------------------------------
    // QR Scanner Launch
    // -------------------------------------------------------------------------

    /**
     * Configures and launches the ZXing QR scanner.
     * Orientation is locked to portrait to avoid camera restarts on rotation.
     */
    private void launchQrScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan Poll QR");
        options.setCameraId(0);
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setBarcodeImageEnabled(false);

        qrScanLauncher.launch(options);
    }

    // -------------------------------------------------------------------------
    // Scan Result Handler
    // -------------------------------------------------------------------------

    /**
     * Callback invoked when the ZXing scanner activity returns.
     *
     * @param result The scan result. {@code result.getContents()} is null if cancelled.
     */
    private void onQrScanResult(ScanIntentResult result) {
        if (result.getContents() == null) {
            Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
            binding.tvScanResult.setText("No QR scanned yet");
            return;
        }

        String scannedText = result.getContents();

        // Show a brief preview of the scanned content
        binding.tvScanResult.setText("Scanned successfully — loading poll…");

        // Pass the raw poll JSON to VoteActivity for parsing and display
        Intent intent = new Intent(ScanPollActivity.this, VoteActivity.class);
        intent.putExtra(AppConstants.EXTRA_POLL_JSON, scannedText);
        startActivity(intent);
    }
}

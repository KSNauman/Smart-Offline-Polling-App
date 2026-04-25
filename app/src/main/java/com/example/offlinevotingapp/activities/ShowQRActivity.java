package com.example.offlinevotingapp.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityShowQrBinding;
import com.example.offlinevotingapp.utils.QRUtils;

public class ShowQRActivity extends AppCompatActivity {

    private ActivityShowQrBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String pollJson = getIntent().getStringExtra(AppConstants.EXTRA_POLL_JSON);
        String title = getIntent().getStringExtra(AppConstants.EXTRA_QR_SCREEN_TITLE);
        String hint = getIntent().getStringExtra(AppConstants.EXTRA_QR_HINT);

        if (title != null && !title.isEmpty()) {
            binding.tvScreenTitle.setText(title);
        }

        if (pollJson != null && !pollJson.isEmpty()) {
            displayQrCode(pollJson, hint);
        } else {
            Toast.makeText(this, "No data received.", Toast.LENGTH_LONG).show();
        }

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnDone.setOnClickListener(v -> finish());
    }

    private void displayQrCode(String json, String hint) {
        Bitmap qrBitmap = QRUtils.generateQRCode(json);
        if (qrBitmap != null) {
            binding.ivQrCode.setImageBitmap(qrBitmap);
            if (hint != null && !hint.isEmpty()) {
                binding.tvQrHint.setText(hint);
            }
        } else {
            Toast.makeText(this, "Failed to generate QR code.", Toast.LENGTH_SHORT).show();
        }
    }
}

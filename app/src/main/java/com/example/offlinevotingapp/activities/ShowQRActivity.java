package com.example.offlinevotingapp.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityShowQrBinding;
import com.example.offlinevotingapp.utils.QRUtils;

/**
 * ShowQRActivity — Displays the generated QR code for a poll.
 *
 * Responsibilities (Phase 2):
 *  - Receive poll JSON string from Intent extra (EXTRA_POLL_JSON)
 *  - Generate a QR code Bitmap via QRUtils.generateQRCode()
 *  - Render the Bitmap into the ImageView
 *  - Display the raw JSON in a TextView for debugging
 *  - Show a Toast if anything goes wrong
 */
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
            binding.tvShowQrTitle.setText(title);
        }

        if (pollJson != null && !pollJson.isEmpty()) {
            displayQrCode(pollJson, hint);
        } else {
            Toast.makeText(this, "No data received.", Toast.LENGTH_LONG).show();
            binding.tvQrPlaceholder.setText("No data received.");
        }
    }

    // -------------------------------------------------------------------------
    // QR Display
    // -------------------------------------------------------------------------

    /**
     * Generates a QR code from the JSON string and renders it.
     * Also shows the raw JSON below the QR for debugging.
     *
     * @param json The serialized JSON to encode into the QR code.
     * @param hint An optional hint text to show below the QR code.
     */
    private void displayQrCode(String json, String hint) {
        Bitmap qrBitmap = QRUtils.generateQRCode(json);

        if (qrBitmap != null) {
            binding.ivQrCode.setImageBitmap(qrBitmap);
            if (hint != null && !hint.isEmpty()) {
                binding.tvQrPlaceholder.setText(hint);
                binding.tvQrPlaceholder.setVisibility(View.VISIBLE);
            } else {
                binding.tvQrPlaceholder.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Failed to generate QR code.", Toast.LENGTH_SHORT).show();
            binding.tvQrPlaceholder.setText("QR generation failed.");
        }

        // Show raw JSON in the debug TextView (with label)
        binding.tvDebugLabel.setVisibility(View.VISIBLE);
        binding.tvPollJson.setText(json);
        binding.tvPollJson.setVisibility(View.VISIBLE);
    }
}

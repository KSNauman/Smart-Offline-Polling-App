package com.example.offlinevotingapp.activities;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityVoteBinding;
import com.example.offlinevotingapp.models.Option;
import com.example.offlinevotingapp.models.Poll;
import com.example.offlinevotingapp.models.Vote;
import com.example.offlinevotingapp.utils.JsonUtils;
import com.example.offlinevotingapp.utils.NFCUtils;

import java.util.List;

/**
 * VoteActivity — Phase 4.
 * Displays parsed poll, captures vote, delivers via QR or NFC.
 */
public class VoteActivity extends AppCompatActivity {

    private static final String TAG = AppConstants.LOG_TAG + "/VoteActivity";

    private ActivityVoteBinding binding;
    private Poll currentPoll;

    // NFC
    private NfcAdapter     nfcAdapter;
    private PendingIntent  nfcPendingIntent;
    private IntentFilter[] nfcIntentFilters;
    private boolean        nfcModeActive      = false;
    private boolean        nfcDispatchEnabled = false;
    private String         pendingVoteJson    = null;

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initNfc();
        loadPollFromIntent();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcModeActive) enableNfcDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableNfcDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!nfcModeActive) return;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag != null && pendingVoteJson != null) {
                writeVoteToTag(tag);
            }
        }
    }

    // -------------------------------------------------------------------------
    // NFC helpers
    // -------------------------------------------------------------------------

    private void initNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) return;

        int flags = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                ? PendingIntent.FLAG_MUTABLE : 0;

        nfcPendingIntent = PendingIntent.getActivity(
                this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                flags);

        nfcIntentFilters = new IntentFilter[]{
                new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        };
    }

    private void enableNfcDispatch() {
        if (nfcAdapter != null && nfcAdapter.isEnabled() && !nfcDispatchEnabled) {
            nfcAdapter.enableForegroundDispatch(
                    this, nfcPendingIntent, nfcIntentFilters, null);
            nfcDispatchEnabled = true;
        }
    }

    private void disableNfcDispatch() {
        if (nfcAdapter != null && nfcDispatchEnabled) {
            nfcAdapter.disableForegroundDispatch(this);
            nfcDispatchEnabled = false;
        }
    }

    // -------------------------------------------------------------------------
    // Poll loading
    // -------------------------------------------------------------------------

    private void loadPollFromIntent() {
        String pollJson = getIntent().getStringExtra(AppConstants.EXTRA_POLL_JSON);
        if (pollJson == null || pollJson.isEmpty()) {
            Toast.makeText(this, "No poll data found.", Toast.LENGTH_LONG).show();
            finish(); return;
        }
        currentPoll = JsonUtils.parsePollFromJson(pollJson);
        if (currentPoll == null) {
            Toast.makeText(this, "Invalid poll QR — could not parse.", Toast.LENGTH_LONG).show();
            finish(); return;
        }
        if (currentPoll.getOptions() == null || currentPoll.getOptions().isEmpty()) {
            Toast.makeText(this, "Poll has no options.", Toast.LENGTH_LONG).show();
            finish(); return;
        }
        populatePollUi(currentPoll);
    }

    // -------------------------------------------------------------------------
    // UI population
    // -------------------------------------------------------------------------

    private void populatePollUi(Poll poll) {
        binding.tvPollQuestion.setText(poll.getQuestion());
        binding.rgVoteOptions.removeAllViews();
        float dp = getResources().getDisplayMetrics().density;
        int pad = (int)(8 * dp);

        for (Option opt : poll.getOptions()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opt.getText());
            rb.setId(View.generateViewId());
            rb.setTag(opt.getOptionId());
            rb.setTextSize(15f);
            rb.setPadding(pad, pad, pad, pad);
            android.widget.RadioGroup.LayoutParams lp = new android.widget.RadioGroup.LayoutParams(
                    android.widget.RadioGroup.LayoutParams.MATCH_PARENT,
                    android.widget.RadioGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = (int)(6 * dp);
            rb.setLayoutParams(lp);
            binding.rgVoteOptions.addView(rb);
        }
    }

    // -------------------------------------------------------------------------
    // Listeners
    // -------------------------------------------------------------------------

    private void setupListeners() {
        binding.btnSubmitVote.setOnClickListener(v -> onSubmitVoteClicked());
    }

    // -------------------------------------------------------------------------
    // Submit handler
    // -------------------------------------------------------------------------

    private void onSubmitVoteClicked() {
        int selectedId = binding.rgVoteOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an option.", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRb = binding.rgVoteOptions.findViewById(selectedId);
        String optionId = (String) selectedRb.getTag();

        // Device ID
        String deviceId = Settings.Secure.getString(
                getContentResolver(), Settings.Secure.ANDROID_ID);

        // Build Vote & serialize
        Vote vote = new Vote(currentPoll.getPollId(), optionId, deviceId);
        String voteJson = JsonUtils.convertVoteToJson(vote);

        if (voteJson == null) {
            Toast.makeText(this, "Error creating vote data.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Vote JSON: " + voteJson);
        showDeliveryDialog(voteJson);
    }

    // -------------------------------------------------------------------------
    // Delivery dialog
    // -------------------------------------------------------------------------

    private void showDeliveryDialog(String voteJson) {
        new AlertDialog.Builder(this)
                .setTitle("Submit Vote")
                .setMessage("How would you like to submit your vote?")
                .setPositiveButton("Show QR", (d, w) -> openVoteQrScreen(voteJson))
                .setNegativeButton("Send via NFC", (d, w) -> startNfcMode(voteJson))
                .setNeutralButton("Cancel", null)
                .show();
    }

    // -------------------------------------------------------------------------
    // QR delivery
    // -------------------------------------------------------------------------

    private void openVoteQrScreen(String voteJson) {
        Intent intent = new Intent(this, ShowQRActivity.class);
        intent.putExtra(AppConstants.EXTRA_POLL_JSON, voteJson);
        intent.putExtra(AppConstants.EXTRA_QR_SCREEN_TITLE, "Your Vote QR Code");
        intent.putExtra(AppConstants.EXTRA_QR_HINT, "Show this QR to the host");
        startActivity(intent);
    }

    // -------------------------------------------------------------------------
    // NFC delivery
    // -------------------------------------------------------------------------

    private void startNfcMode(String voteJson) {
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC not supported on this device.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled. Enable it in Settings.", Toast.LENGTH_LONG).show();
            return;
        }
        pendingVoteJson = voteJson;
        nfcModeActive   = true;
        enableNfcDispatch();
        Toast.makeText(this, "Tap host device to send vote.", Toast.LENGTH_LONG).show();
    }

    private void writeVoteToTag(Tag tag) {
        NdefMessage msg = NFCUtils.createNdefMessage(pendingVoteJson);
        if (msg == null) {
            Toast.makeText(this, "Failed to create NFC message.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean ok = NFCUtils.writeNdefToTag(tag, msg);
        if (ok) {
            Toast.makeText(this, "✅ Vote sent via NFC!", Toast.LENGTH_LONG).show();
            nfcModeActive   = false;
            pendingVoteJson = null;
            disableNfcDispatch();
        } else {
            Toast.makeText(this, "NFC write failed — tap again.", Toast.LENGTH_LONG).show();
        }
    }
}

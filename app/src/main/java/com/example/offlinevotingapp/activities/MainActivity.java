package com.example.offlinevotingapp.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlinevotingapp.constants.AppConstants;
import com.example.offlinevotingapp.databinding.ActivityMainBinding;
import com.example.offlinevotingapp.managers.VoteManager;
import com.example.offlinevotingapp.models.Vote;
import com.example.offlinevotingapp.utils.JsonUtils;
import com.example.offlinevotingapp.utils.NFCUtils;

/**
 * MainActivity — Entry point of the app.
 *
 * Responsibilities (Phase 1):
 *  - Display "Create Poll" and "Scan Poll" buttons
 *  - Navigate to CreatePollActivity and ScanPollActivity respectively
 *  - Initialize NfcAdapter and set up foreground dispatch system
 *  - Handle NFC intents via onNewIntent()
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = AppConstants.LOG_TAG + "/MainActivity";

    // ViewBinding
    private ActivityMainBinding binding;

    // NFC
    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[] nfcIntentFilters;

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout via ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNfc();
        setupButtonListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableNfcForegroundDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableNfcForegroundDispatch();
    }

    /**
     * Called when the activity receives a new Intent while running in the foreground.
     * This is triggered by NFC tag discovery when foreground dispatch is active.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNfcIntent(intent);
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    private void setupButtonListeners() {
        binding.btnCreatePoll.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreatePollActivity.class);
            startActivity(intent);
        });

        binding.btnScanPoll.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanPollActivity.class);
            startActivity(intent);
        });

        binding.btnScanVote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanVoteActivity.class);
            startActivity(intent);
        });

        binding.btnViewResults.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            startActivity(intent);
        });
    }

    // -------------------------------------------------------------------------
    // NFC Setup — Foreground Dispatch System
    // -------------------------------------------------------------------------

    /**
     * Initializes the NfcAdapter and builds the foreground dispatch PendingIntent
     * and intent filters needed to intercept NFC NDEF events while the app is open.
     */
    private void initNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Log.w(TAG, "initNfc: device does not support NFC");
            Toast.makeText(this, "NFC not available on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            Log.w(TAG, "initNfc: NFC is disabled");
            Toast.makeText(this, "Please enable NFC in Settings", Toast.LENGTH_SHORT).show();
        }

        // PendingIntent that will be delivered to this activity when an NFC tag is scanned
        int flags = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S
                ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
                : PendingIntent.FLAG_UPDATE_CURRENT;

        nfcPendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                flags
        );

        // Filter for NDEF_DISCOVERED with our MIME type
        IntentFilter ndefFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefFilter.addDataType(AppConstants.NFC_MIME_TYPE);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Log.e(TAG, "initNfc: malformed MIME type", e);
        }

        nfcIntentFilters = new IntentFilter[]{ndefFilter};

        Log.d(TAG, "initNfc: NFC initialized successfully");
    }

    /**
     * Enables NFC foreground dispatch — app intercepts NFC events before the system.
     * Called in onResume().
     */
    private void enableNfcForegroundDispatch() {
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, nfcIntentFilters, null);
            Log.d(TAG, "enableNfcForegroundDispatch: enabled");
        }
    }

    /**
     * Disables NFC foreground dispatch to free resources.
     * Called in onPause().
     */
    private void disableNfcForegroundDispatch() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
            Log.d(TAG, "disableNfcForegroundDispatch: disabled");
        }
    }

    // -------------------------------------------------------------------------
    // NFC Intent Handling
    // -------------------------------------------------------------------------

    /**
     * Processes an NFC intent received via foreground dispatch or onNewIntent().
     * Phase 1 — reads and logs the NDEF message; full routing added in Phase 2.
     *
     * @param intent The NFC intent to handle.
     */
    private void handleNfcIntent(Intent intent) {
        if (intent == null) return;

        String action = intent.getAction();
        Log.d(TAG, "handleNfcIntent: action=" + action);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMessages != null && rawMessages.length > 0) {
                NdefMessage ndefMessage = (NdefMessage) rawMessages[0];
                String payload = NFCUtils.readNdefMessage(ndefMessage);
                Log.d(TAG, "handleNfcIntent: payload=" + payload);

                // Phase 5: Parse payload as Vote
                Vote vote = JsonUtils.parseVoteFromJson(payload);
                if (vote != null) {
                    VoteManager voteManager = VoteManager.getInstance();
                    if (voteManager.hasVoted(vote.getDeviceId())) {
                        Toast.makeText(this, "Duplicate vote ignored", Toast.LENGTH_SHORT).show();
                    } else {
                        voteManager.addVote(vote.getOptionId(), vote.getDeviceId());
                        Toast.makeText(this, "Vote received via NFC", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "handleNfcIntent: Payload is not a valid Vote JSON");
                }
            } else {
                Log.d(TAG, "handleNfcIntent: no NDEF messages found");
            }
        }
    }
}

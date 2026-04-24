package com.example.offlinevotingapp.utils;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.util.Log;

import com.example.offlinevotingapp.constants.AppConstants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for NFC NDEF operations.
 * Phase 1 — structure and message creation/reading stubs.
 * Full send/receive logic will be implemented in Phase 2.
 */
public class NFCUtils {

    private static final String TAG = AppConstants.LOG_TAG + "/NFCUtils";

    // Private constructor — static utility class
    private NFCUtils() {}

    /**
     * Creates an NDEF message containing a single plain-text record.
     *
     * @param content The string payload to encode in the NDEF record.
     * @return An NdefMessage ready to be written to an NFC tag, or null on error.
     */
    public static NdefMessage createNdefMessage(String content) {
        if (content == null || content.isEmpty()) {
            Log.w(TAG, "createNdefMessage: content is null or empty");
            return null;
        }

        try {
            // Build a MIME-type record with text/plain
            byte[] mimeBytes    = AppConstants.NFC_MIME_TYPE.getBytes(StandardCharsets.US_ASCII);
            byte[] payloadBytes = content.getBytes(StandardCharsets.UTF_8);

            NdefRecord record = new NdefRecord(
                    NdefRecord.TNF_MIME_MEDIA,
                    mimeBytes,
                    new byte[0],   // no ID
                    payloadBytes
            );

            return new NdefMessage(new NdefRecord[]{record});

        } catch (Exception e) {
            Log.e(TAG, "createNdefMessage: failed to build NDEF message", e);
            return null;
        }
    }

    /**
     * Reads the first NDEF record from an NdefMessage and returns its payload as a String.
     *
     * @param ndefMessage The NdefMessage read from an NFC intent.
     * @return The decoded string payload, or null if unreadable.
     */
    public static String readNdefMessage(NdefMessage ndefMessage) {
        if (ndefMessage == null) {
            Log.w(TAG, "readNdefMessage: ndefMessage is null");
            return null;
        }

        NdefRecord[] records = ndefMessage.getRecords();
        if (records == null || records.length == 0) {
            Log.w(TAG, "readNdefMessage: no records found");
            return null;
        }

        try {
            // Read first record payload
            byte[] payload = records[0].getPayload();
            return new String(payload, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(TAG, "readNdefMessage: failed to parse payload", e);
            return null;
        }
    }

    /**
     * Stub — writes an NdefMessage to a discovered NFC tag.
     * Full implementation in Phase 2.
     *
     * @param tag         The NFC Tag to write to.
     * @param ndefMessage The message to write.
     * @return true if successful (always false in Phase 1 stub).
     */
    public static boolean writeNdefToTag(Tag tag, NdefMessage ndefMessage) {
        if (tag == null || ndefMessage == null) return false;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                
                if (!ndef.isWritable()) {
                    Log.e(TAG, "writeNdefToTag: tag is read-only");
                    ndef.close();
                    return false;
                }
                
                if (ndef.getMaxSize() < ndefMessage.toByteArray().length) {
                    Log.e(TAG, "writeNdefToTag: tag capacity is too small");
                    ndef.close();
                    return false;
                }
                
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Log.d(TAG, "writeNdefToTag: write successful");
                return true;
            } else {
                Log.e(TAG, "writeNdefToTag: NDEF is not supported by this Tag");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "writeNdefToTag: Exception while writing NDEF", e);
            return false;
        }
    }

    /**
     * Checks if the device supports NFC.
     *
     * @param nfcAdapter The NfcAdapter from the activity (may be null if no NFC hardware).
     * @return true if NFC is available and enabled.
     */
    public static boolean isNfcAvailable(NfcAdapter nfcAdapter) {
        return nfcAdapter != null && nfcAdapter.isEnabled();
    }
}

package com.example.offlinevotingapp.constants;

/**
 * Application-wide constants.
 * Phase 1 — foundational keys and defaults.
 * Phase 3 — added EXTRA_SELECTED_OPTION_ID.
 */
public final class AppConstants {

    // Prevent instantiation
    private AppConstants() {}

    // Intent extra keys
    public static final String EXTRA_POLL_ID            = "extra_poll_id";
    public static final String EXTRA_POLL_JSON          = "extra_poll_json";
    public static final String EXTRA_QR_DATA            = "extra_qr_data";
    public static final String EXTRA_SELECTED_OPTION_ID = "extra_selected_option_id";
    public static final String EXTRA_QR_SCREEN_TITLE    = "extra_qr_screen_title";
    public static final String EXTRA_QR_HINT            = "extra_qr_hint";

    // NFC MIME type used for NDEF messages
    public static final String NFC_MIME_TYPE     = "text/plain";

    // QR code dimensions (pixels)
    public static final int QR_WIDTH             = 512;
    public static final int QR_HEIGHT            = 512;

    // Default tag used in logs
    public static final String LOG_TAG           = "OfflineVotingApp";

    // Request codes
    public static final int REQUEST_CAMERA_PERMISSION = 100;
    public static final int REQUEST_QR_SCAN          = 200;
}

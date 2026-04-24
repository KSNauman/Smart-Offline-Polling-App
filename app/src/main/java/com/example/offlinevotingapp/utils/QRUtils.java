package com.example.offlinevotingapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import com.example.offlinevotingapp.constants.AppConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for QR Code generation.
 *
 * Phase 2:
 *  - generateQRCode(String text) — primary public API used by ShowQRActivity
 *  - generateQRBitmap(String content) — kept for backward compatibility
 *  - scanQR() — stub; full ZXing scan integration in Phase 3
 */
public class QRUtils {

    // Private constructor — static utility class
    private QRUtils() {}

    // -------------------------------------------------------------------------
    // QR Code Generation — Phase 2 primary API
    // -------------------------------------------------------------------------

    /**
     * Generates a QR code Bitmap that encodes the given text string.
     * Uses ZXing's QRCodeWriter with medium error correction (level M).
     *
     * @param text The string to encode (typically a poll JSON payload).
     * @return A {@link Bitmap} of size {@link AppConstants#QR_WIDTH} x
     *         {@link AppConstants#QR_HEIGHT}, or {@code null} if encoding fails
     *         (e.g. empty input or ZXing WriterException).
     */
    public static Bitmap generateQRCode(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 2);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    AppConstants.QR_WIDTH,
                    AppConstants.QR_HEIGHT,
                    hints
            );

            int width  = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Alias kept for backward compatibility with any existing callers.
     * Delegates to {@link #generateQRCode(String)}.
     *
     * @param content The string to encode.
     * @return QR code Bitmap, or null on failure.
     */
    public static Bitmap generateQRBitmap(String content) {
        return generateQRCode(content);
    }

    // -------------------------------------------------------------------------
    // QR Code Scanning — stub for Phase 3
    // -------------------------------------------------------------------------

    /**
     * Stub for QR scanning. Full implementation (ZXing IntentIntegrator) in Phase 3.
     *
     * @return Placeholder string.
     */
    public static String scanQR() {
        // TODO Phase 3: Use IntentIntegrator to launch ZXing scanner activity
        return "SCAN_STUB_NOT_IMPLEMENTED";
    }
}

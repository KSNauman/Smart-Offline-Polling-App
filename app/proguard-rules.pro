# Default ProGuard rules for Android.
# Add project-specific rules here when Phase 2 logic is added.

# Keep ZXing classes
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.** { *; }

# Keep NFC-related classes
-keep class android.nfc.** { *; }

# Keep app model classes (for future JSON serialization)
-keep class com.example.offlinevotingapp.models.** { *; }

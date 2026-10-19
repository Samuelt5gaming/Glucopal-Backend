package com.diabetesapp.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Initialises the Firebase Admin SDK from a service-account key supplied as an env var.
 * Set ONE of: FIREBASE_SERVICE_ACCOUNT_B64 (base64 of the JSON file, easiest on Koyeb)
 * or FIREBASE_SERVICE_ACCOUNT_JSON (raw JSON).
 */
@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp(
            @Value("${firebase.service-account-b64:}") String b64,
            @Value("${firebase.service-account-json:}") String json) throws IOException {

        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        byte[] credentialBytes;
        if (!b64.isBlank()) {
            credentialBytes = Base64.getDecoder().decode(b64.trim());
        } else if (!json.isBlank()) {
            credentialBytes = json.getBytes(StandardCharsets.UTF_8);
        } else {
            throw new IllegalStateException(
                "Firebase credentials missing: set FIREBASE_SERVICE_ACCOUNT_B64 or FIREBASE_SERVICE_ACCOUNT_JSON");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(credentialBytes)))
                .build();
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp app) {
        return FirebaseAuth.getInstance(app);
    }
}

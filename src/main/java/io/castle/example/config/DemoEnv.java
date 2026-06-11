package io.castle.example.config;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Resolves configuration and the simulated demo fixture. Only castle_pk and
 * castle_api_secret need to be provided (via the environment or a local .env
 * file); the "valid user" the demo logs in falls back to baked-in defaults.
 */
@Component
public class DemoEnv {

    private final Map<String, String> values = new HashMap<>();

    public DemoEnv() {
        // Baked-in fixture defaults.
        values.put("location", "localhost");
        values.put("valid_username", "clark.kent@dailyplanet.com");
        values.put("valid_name", "Clark Kent");
        values.put("valid_user_id", "00000000");
        values.put("valid_password", "1234");
        values.put("invalid_password", "qwerty");
        values.put("webhook_url", "https://webhook.site");

        // A local .env file (if present) overrides the defaults.
        loadDotEnv(Paths.get(".env"));

        // The real environment takes precedence over everything else.
        System.getenv().forEach((k, v) -> {
            if (v != null && !v.isEmpty()) {
                values.put(k, v);
            }
        });
    }

    private void loadDotEnv(Path path) {
        if (!Files.isReadable(path)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int eq = trimmed.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String key = trimmed.substring(0, eq).trim();
                String value = trimmed.substring(eq + 1).trim();
                if (!value.isEmpty()) {
                    values.put(key, value);
                }
            }
        } catch (IOException ignored) {
            // A missing or unreadable .env file is not fatal.
        }
    }

    public String get(String key) {
        return values.get(key);
    }

    public String castlePk() {
        return values.get("castle_pk");
    }

    public String castleApiSecret() {
        return values.get("castle_api_secret");
    }
}

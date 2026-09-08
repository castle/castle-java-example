package io.castle.example.config;

import io.castle.client.Castle;
import io.castle.client.model.CastleSdkConfigurationException;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Initialises the Castle SDK singleton once, on startup. The API secret comes
 * from the dashboard (Settings → API).
 */
@Configuration
public class CastleConfig {

    private final DemoEnv env;

    public CastleConfig(DemoEnv env) {
        this.env = env;
    }

    @PostConstruct
    public void initialize() {
        String secret = env.castleApiSecret();
        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException("castle_api_secret is required");
        }
        try {
            Castle castle = Castle.initialize(
                    Castle.configurationBuilder()
                            .apiSecret(secret)
                            // Request timeout in ms.
                            .withTimeout(1500)
                            .build());
            Castle.setSingletonInstance(castle);
        } catch (CastleSdkConfigurationException e) {
            throw new IllegalStateException("The Castle SDK configuration is not correct", e);
        }
    }
}

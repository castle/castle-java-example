package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.internal.backend.CastleBackendProvider;
import io.castle.client.model.AuthenticateAction;
import io.castle.client.model.AuthenticateFailoverStrategy;
import io.castle.client.model.CastleSdkConfigurationException;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.Arrays;

public class SetupListener implements ServletContextListener {

    /**
     * Example of how to verify the SDK configuration during application initialization to avoid errors
     * during request handling.
     *
     * @param sce
     */
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Castle castle = Castle.verifySdkConfigurationAndInitialize();

            /*Castle castle = Castle.initialize(Castle.configurationBuilder()
                    .apiSecret("abcd")
                    .withAllowListHeaders("User-Agent", "Accept-Language", "Accept-Encoding")
                    .withDenyListHeaders("Cookie")
                    .withTimeout(500)
                    .withBackendProvider(CastleBackendProvider.OKHTTP)
                    .withAuthenticateFailoverStrategy(new AuthenticateFailoverStrategy(AuthenticateAction.ALLOW))
                    .withApiBaseUrl("https://api.castle.io/")
                    .withLogHttpRequests(true)
                    .ipHeaders(Arrays.asList("X-Forwarded-For", "CF-Connecting-IP"))
                    .build());*/

            Castle.setSingletonInstance(castle);
        } catch (CastleSdkConfigurationException e) {
            //The sdk configuration is incorrect. We recommend to shut down the application by throwing a runtime exception
            throw new IllegalStateException("The Castle SDK configuration is not correct", e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}

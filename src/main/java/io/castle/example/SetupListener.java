package io.castle.example;

import io.castle.client.Castle;
import io.castle.client.model.CastleSdkConfigurationException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SetupListener implements ServletContextListener {

    /**
     * Example of how to verify the SDK configuration during application initialization to avoid errors
     * during request handling.
     *
     * @param sce
     */
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Castle.verifySdkConfigurationAndInitialize();
        } catch (CastleSdkConfigurationException e) {
            //The sdk configuration is incorrect. We recommend to shutdown the application by throwing a runtime exception
            throw new IllegalStateException("The Castle SDK configuration is not correct", e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}

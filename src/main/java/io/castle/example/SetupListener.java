package io.castle.example;

import io.castle.client.Castle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SetupListener implements ServletContextListener {

    /**
     * Example of how to verify the SDK configuratino during application initialization to avoid errors during request handling.
     * @param sce
     */
    public void contextInitialized(ServletContextEvent sce) {
        Castle.verifySdkConfiguration();
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}

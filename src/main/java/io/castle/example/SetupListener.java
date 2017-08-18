package io.castle.example;

import io.castle.client.Castle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SetupListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        //Verify that the castle SDK configuration is loaded correctly
        //TODO now we have a exception, create a verify method with a setup problem report.
        Castle.sdk();
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}

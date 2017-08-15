package io.castle.example.utils;

public class Config {

    //TODO provide the app id value on the SDK configuration values
    public static String appId() {
        return System.getenv("CASTLE_APP_ID");
    }
}

package io.castle.example.utils;

public class Config {

    public static String apiKey() {
        return System.getenv("CASTLE_API_SECRET");
    }

    public static String appId() {
        return System.getenv("CASTLE_APP_ID");
    }
}

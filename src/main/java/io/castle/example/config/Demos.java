package io.castle.example.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The ordered list of workflows shown in the navbar and on the home page.
 */
public final class Demos {

    public static final List<Demo> LIST = List.of(
            new Demo("signup", "sign up",
                    "Filter a registration ($registration) before the account exists.", null),
            new Demo("login", "login",
                    "Filter the attempt, then assess a successful login with Risk.",
                    "https://www.websequencediagrams.com/files/render?link=Q9WYp8rNThVZhA1inf2FSLfjChYZTdHXyGB9zqvMNpsaAvKvJPARgo5LI5fM5K4D"),
            new Demo("account", "account",
                    "Update your profile, send a custom event, and log out.", null),
            new Demo("password_reset", "password reset",
                    "Record a password-reset event with the non-blocking log endpoint.", null),
            new Demo("lists", "lists",
                    "Create and fetch lists with the Lists API.", null),
            new Demo("privacy", "privacy",
                    "Request or delete a user's data with the Privacy API.", null),
            new Demo("webhooks", "webhooks",
                    "Verify and inspect incoming Castle webhooks.", null));

    private static final Map<String, Demo> BY_URL = new LinkedHashMap<>();

    static {
        for (Demo d : LIST) {
            BY_URL.put(d.getUrl(), d);
        }
    }

    private Demos() {
    }

    public static Demo byUrl(String url) {
        return BY_URL.get(url);
    }
}

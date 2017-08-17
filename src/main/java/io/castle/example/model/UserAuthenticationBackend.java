package io.castle.example.model;

import java.util.HashMap;
import java.util.Map;

public class UserAuthenticationBackend {

    static Map<String, TestUser> testUsersByLogin = new HashMap<String, TestUser>() {
        {
            TestUser admin = new TestUser("admin@example.com", "admin", "admin");
            TestUser josh = new TestUser("josh@example.com", "anyPassword", "Josh");
            put(admin.getLogin(), admin);
            put(josh.getLogin(), josh);
        }
    };

    static void addUser(TestUser user) {
        testUsersByLogin.put(user.getLogin(), user);
    }

    public static TestUser findUser(String userLogin) {
        return testUsersByLogin.get(userLogin);
    }

}

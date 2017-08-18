package io.castle.example.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UserAuthenticationBackend {

    static AtomicInteger idCounter = new AtomicInteger();

    static Map<String, TestUser> testUsersByLogin = new HashMap<String, TestUser>() {
        {
            TestUser admin = createNewUser("admin@example.com", "admin", "Jane", "Doe");
            TestUser josh = createNewUser("josh@example.com", "anyPassword", "Josh", "Bond");
            put(admin.getLogin(), admin);
            put(josh.getLogin(), josh);
        }
    };

    static void addUpdateUser(TestUser user) {
        testUsersByLogin.put(user.getLogin(), user);
    }

    public static void updateUserLogin(String currentLogin, String newLogin) throws Exception {
        TestUser userData = findUser(currentLogin);
        if (userData != null) {
            userData.setLogin(newLogin);
            testUsersByLogin.remove(currentLogin);
            addUpdateUser(userData);
        } else {
            throw new Exception("No such user");
        }
    }

    public static void updateUserPassword(String login, String password) throws Exception {
        TestUser userData = findUser(login);
        if (userData != null) {
            userData.setPassword(password);
            addUpdateUser(userData);
        } else {
            throw new Exception("No such user");
        }
    }

    public static TestUser findUser(String userLogin) {
        return testUsersByLogin.get(userLogin);
    }

    public static TestUser createNewUser(String login, String password, String name, String lastname) {
        return new TestUser(getFreshInteger(), login, password, name, lastname);
    }

    private static Integer getFreshInteger() {
        return Integer.valueOf(idCounter.getAndIncrement());
    }

}

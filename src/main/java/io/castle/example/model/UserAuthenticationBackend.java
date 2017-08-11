package io.castle.example.model;

import java.util.HashMap;
import java.util.Map;

public class UserAuthenticationBackend {

    Map<String,TestUser> testUsersByLogin;

    public UserAuthenticationBackend() {
        this.testUsersByLogin = new HashMap<String, TestUser>();
        initTestUsers();
    }

    private void initTestUsers() {
        addUser(new TestUser("admin@example.com","admin", "admin"));
        addUser(new TestUser("josh@example.com","anyPassword", "Josh"));
    }

    private void addUser(TestUser user) {
        this.testUsersByLogin.put(user.getLogin(),user);
    }

    public TestUser findUser(String userLogin) {
        return testUsersByLogin.get(userLogin);
    }

}

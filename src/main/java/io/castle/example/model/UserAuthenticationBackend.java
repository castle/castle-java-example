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
        addUser(new TestUser("admin","admin"));
        addUser(new TestUser("josh","anyPassword"));
    }

    private void addUser(TestUser user) {
        this.testUsersByLogin.put(user.getLogin(),user);
    }

    public TestUser findUser(String username) {
        return testUsersByLogin.get(username);
    }
}

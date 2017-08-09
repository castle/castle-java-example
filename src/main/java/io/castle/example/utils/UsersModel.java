package io.castle.example.utils;

import java.util.HashMap;

public class UsersModel {

    public HashMap<String, String> users;

    public UsersModel() {
        users = new HashMap<String, String>();
        this.users.put("John", "1234");
        this.users.put("Jane", "4321");
    }
}

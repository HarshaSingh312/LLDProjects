package org.example.services;

import org.example.models.User;

import java.util.HashMap;

public class UserService {

    private HashMap<String, User> userDB = new HashMap<>();

    public String registerUser(String name) {
        if (userDB.containsKey(name)) return "User already registered";
        userDB.put(name, new User(name));
        return "User registered with id: " + name;
    }

    public boolean isUserRegistered(String name) {
        return userDB.containsKey(name);
    }
}

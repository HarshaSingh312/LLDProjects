package org.example.service;

import org.example.model.BorrowBook;
import org.example.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    Map<String, User> usersDB = new HashMap<>();

    public UserService() {
    }

    public boolean isValidUser(String userId){
        return usersDB.containsKey(userId);
    }

    public String addUser(String userId, String name) {
        if (userId.isBlank() || name.isBlank()) return "INVALID_INPUT";
        if (usersDB.containsKey(userId)) return "USER_ALREADY_EXISTS";
        usersDB.put(userId, new User(userId, name));
        return "SUCCESS";
    }

    public String removeUser(String userId) {
        if (userId.isBlank()) return "INVALID_INPUT";
        if (!usersDB.containsKey(userId)) return "USER_NOT_FOUND";
        usersDB.remove(userId);
        return "SUCCESS";
    }
}

package org.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WaitListService {

    // bookId to WaitList map
    HashMap<String, List<String>> waitingList = new HashMap<>();

    public boolean isUserInWaitList(String userName) {
        for (List<String> Users: waitingList.values()) {
            for (String user: Users) {
                if (user.equals(userName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getFirstUser(String bookId) {
        if (!waitingList.containsKey(bookId) || waitingList.get(bookId).isEmpty()) {
            return null;
        }
        return waitingList.get(bookId).get(0);
    }

    public void removeFirstUser(String bookId) {
        if (!waitingList.containsKey(bookId) || waitingList.get(bookId).isEmpty()) {
            return;
        }
        List<String> users = waitingList.get(bookId);
        users.remove(0);
        waitingList.put(bookId, users);
//        return user;
    }

    public boolean isUserInWaitList(String userName, String bookId) {
        return waitingList.getOrDefault(bookId, new ArrayList<>()).contains(userName);
    }

    public String addToWaitList(String userId, String bookId) {
        if (isUserInWaitList(userId, bookId)) return "ALREADY_WAITLISTED";
        List<String> users = waitingList.getOrDefault(bookId, new ArrayList<>());
        users.add(userId);
        waitingList.put(bookId, users);
        return "WAITLISTED," + users.size();
    }
}

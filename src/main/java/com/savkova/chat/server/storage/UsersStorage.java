package com.savkova.chat.server.storage;

import com.savkova.chat.server.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersStorage {
    private static final UsersStorage USERS = new UsersStorage();

    private final Map<String, User> userMap = new HashMap<>();

    private UsersStorage() {
    }

    public static UsersStorage getInstance() {
        return USERS;
    }

    public synchronized void add(User user) {
        userMap.put(user.getName(), user);
    }

    public synchronized User getUser(String name) {
        if (userMap.containsKey(name))
            return userMap.get(name);

        return null;
    }

    public synchronized List<User> getAllUsers() {

        List<User> list = new ArrayList<>();
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }

}

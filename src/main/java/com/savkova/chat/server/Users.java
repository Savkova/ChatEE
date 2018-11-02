package com.savkova.chat.server;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private static final Users USERS = new Users();

    private final Map<String, User> userMap = new HashMap<>();

    private Users() {
        // TODO: delete when authorization was done
        userMap.put("user1", new User("user1", "qwerty"));
    }

    public static Users getInstance() {
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
}

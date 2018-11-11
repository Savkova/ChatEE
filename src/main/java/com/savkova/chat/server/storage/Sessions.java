package com.savkova.chat.server.storage;

import java.util.HashMap;
import java.util.Map;

public class Sessions {
    private static final Sessions SESSIONS = new Sessions();

    private final Map<String, String> sessionMap = new HashMap<>();

    private Sessions() {}

    public static Sessions getInstance() {
        return SESSIONS;
    }

    public synchronized boolean add(String userName, String sessionId) {
        if (sessionMap.containsKey(userName)) {
            return false;
        } else {
            sessionMap.put(userName, sessionId);
            return true;
        }
    }

    public synchronized String getSessionId(String userName) {
        return sessionMap.get(userName);
    }

}

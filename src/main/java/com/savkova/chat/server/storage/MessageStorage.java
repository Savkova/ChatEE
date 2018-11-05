package com.savkova.chat.server.storage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.savkova.chat.server.util.JsonMessages;
import com.savkova.chat.server.entities.Message;

import static com.savkova.chat.server.util.Properties.ALL;

public class MessageStorage {
    private static final MessageStorage messageStorage = new MessageStorage();

    private final Gson gson;
    private final Map<String, List<Message>> map = new HashMap<>();

    public static MessageStorage getInstance() {
        return messageStorage;
    }

    private MessageStorage() {
        gson = new GsonBuilder().create();
        map.put(ALL, new LinkedList<>());
    }

    public synchronized void add(Message m, String to) {
        if (map.containsKey(to)) {
            map.get(to).add(m);
        } else {
            List<Message> list = new LinkedList<>();
            list.add(m);
            map.put(to, list);
        }
    }

    public synchronized String toJSON(String to) {
        List<Message> list = null;
        if (map.containsKey(to)) {
            list = map.get(to);
        }

        if (list == null || list.isEmpty()) return "";

        return gson.toJson(new JsonMessages(list));
    }

    public void addRoom(String room) {
        if (!map.containsKey(room))
            map.put(room, new LinkedList<>());
    }

}

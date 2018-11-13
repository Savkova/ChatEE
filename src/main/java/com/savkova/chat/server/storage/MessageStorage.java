package com.savkova.chat.server.storage;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.savkova.chat.server.util.JsonMessages;
import com.savkova.chat.server.entities.Message;

import static com.savkova.chat.server.util.Constants.ALL;

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
        LinkedList<Message> list;

        if (map.containsKey(to)) {
            list = (LinkedList<Message>) map.get(to);

            list.add(m);

        } else {
            list = new LinkedList<>();
            list.add(m);
            map.put(to, list);
        }

    }

    public synchronized void addRoom(String room) {
        if (!map.containsKey(room))
            map.put(room, new LinkedList<>());
    }

    public String toJSON(String to) {
        List<Message> list = null;
        if (map.containsKey(to)) {
            list = map.get(to);
        }

        if (list == null || list.isEmpty()) return "";

        return gson.toJson(new JsonMessages(list));
    }

    public void clearMessageList(String name){
        map.get(name).clear();
    }

}

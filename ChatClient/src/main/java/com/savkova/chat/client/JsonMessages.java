package com.savkova.chat.client;

import java.util.Collections;
import java.util.List;

public class JsonMessages {
    private final List<Message> list;

    public JsonMessages(List<Message> sourceList) {
        this.list = sourceList;
    }

    public List<Message> getList() {
        return Collections.unmodifiableList(list);
    }
}

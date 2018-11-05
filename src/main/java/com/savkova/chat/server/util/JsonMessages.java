package com.savkova.chat.server.util;

import com.savkova.chat.server.entities.Message;

import java.util.List;

public class JsonMessages {
    private final List<Message> list;

    public JsonMessages(List<Message> sourceList) {
        this.list = sourceList;
    }
}

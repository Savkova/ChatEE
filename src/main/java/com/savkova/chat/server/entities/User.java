package com.savkova.chat.server.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.savkova.chat.server.util.Properties.*;

public class User {
    private String name;
    private String password;
    private Set<String> rooms;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.rooms = new HashSet<>();
        this.rooms.add(ALL);
        this.rooms.add(name);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRooms() {
        return rooms;
    }

    public void addRoom(String room) {
        rooms.add(room);
    }

    public void removeRoom(String room) {
        rooms.remove(room);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}

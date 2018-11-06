package com.savkova.chat.server.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.savkova.chat.server.util.Properties.*;

public class User {
    private String name;
    private String password;
    private Set<String> rooms;
    private boolean status;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.rooms = new HashSet<>();
        this.rooms.add(ALL);
        this.rooms.add(name);
        this.status = false;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    @Override
    public String toString() {
        String sStatus = status ? "online" : "offline";

        final StringBuilder sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", status=").append(sStatus);
        sb.append('}');
        return sb.toString();
    }

}

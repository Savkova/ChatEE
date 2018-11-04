package com.savkova.chat.client;

public enum MenuOptions {
    signUp(1, "Sign Up"),
    logIn(2, "Log In"),
    quit(3, "Quit");

    private int id;
    private String name;

    MenuOptions(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static void show() {
        for (MenuOptions item : values()) {
            System.out.println(item.id + " - " + item.name);
        }
    }
}

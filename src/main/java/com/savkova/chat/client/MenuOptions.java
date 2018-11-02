package com.savkova.chat.client;

public enum MenuOptions {
    logIn(1, "Log In"),
    signUp(2, "Sign Up"),
    logOut(3, "Log Out"),
    quit(4, "Quit");

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

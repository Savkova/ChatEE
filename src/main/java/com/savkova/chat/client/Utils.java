package com.savkova.chat.client;

public class Utils {
    public static final String ACCOUNT = "account";
    public static final String LOGIN = "login";
    public static final String PASS = "password";

    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public static String getURL() {
        return URL + ":" + PORT;
    }
}

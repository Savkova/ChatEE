package com.savkova.chat.client;

public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public static final String ACCOUNT = "account";
    public static final String LOGIN = "login";
    public static final String PASS = "password";

    public static final String ALL = "all";
    public static final String LOGOUT = "logout";
    public static final String JOIN = "join";
    public static final String LEAVE = "leave";
    public static final String privateMessageMarker = "@";
    public static final String actionMarker = "#";
    public static final String roomMessageMarker = "$";
    public static final String delimiter = ":";


    public static String getURL() {
        return URL + ":" + PORT;
    }
}

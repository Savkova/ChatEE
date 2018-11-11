package com.savkova.chat.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.savkova.chat.client.Utils.*;

public class ConsoleClient {
    static Scanner scanner = new Scanner(System.in);
    static String sessionId;

    public static void main(String[] args) {
        System.out.println("Welcome to ChatEE!");
        MenuOptions.show();

        try {
            while (true) {
                int actionNumber = askIdMenuOption();
                switch (actionNumber) {
                    case 1:
                        createAccount(); break;
                    case 2:
                        login(); break;
                    case 3:
                        showAllUsers(); break;
                    case 4:
                        System.out.println("Bye!");
                        System.exit(0);
                    default:
                        return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static int askIdMenuOption() {
        int menuLength = MenuOptions.values().length;

        System.out.print("\nSelect the action: ");
        int actionNumber = 0;
        while (true) {
            try {
                actionNumber = Integer.parseInt(scanner.nextLine());
                if ((actionNumber > 0) && (actionNumber <= menuLength))
                    break;
                System.out.println("Invalid input. Select a number from 1 to " + menuLength + ": ");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again: ");
            }
        }
        return actionNumber;
    }

    private static void createAccount() throws IOException {

        System.out.println("\nFor creating new account ");

        System.out.print("enter your login: ");
        String userName = scanner.nextLine();

        if (!userName.equals("") && !userName.equals(ALL)) {
            System.out.print("enter your password: ");
            String password = scanner.nextLine();

            boolean succeeded = isAccountCreated(userName, password);
            if (succeeded) {
                System.out.println("New account created - login '" + userName + "'");

                if (isLogin(userName, password))
                    startChat(userName);

            } else {
                System.out.println("That username is taken. Try another.");
            }
        } else {
            System.out.println("Invalid input. Login is not entered");
        }
    }

    private static boolean isAccountCreated(String userName, String password) throws IOException {

        URL url = new URL(Utils.getURL() + "/chat/signup");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty(LOGIN, userName);
        conn.setRequestProperty(PASS, password);
        conn.connect();

        return (conn.getHeaderField(ACCOUNT).equals("created"));
    }

    private static void login() throws IOException {

        System.out.print("Enter your login: ");
        String userName = scanner.nextLine();

        if (!userName.equals("")) {
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            boolean succeeded = isLogin(userName, password);
            if (succeeded) {
                startChat(userName);
            } else {
                System.out.println("Couldn't find your account or wrong password.");
            }
        } else {
            System.out.println("Invalid input. Login is not entered");
        }
    }

    private static boolean isLogin(String userName, String password) throws IOException {
        URL url;
        HttpURLConnection conn;

        url = new URL(Utils.getURL() + "/chat/login");
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty(LOGIN, userName);
        conn.setRequestProperty(PASS, password);
        conn.connect();

        sessionId = conn.getHeaderField("Set-Cookie");

        return (conn.getHeaderField(LOGIN).equals("true"))
                && (conn.getHeaderField(PASS).equals("true"));

    }

    private static void startChat(String userName) throws IOException {
        GetMessagesThread getMessagesThread = new GetMessagesThread(userName);
        Thread th = new Thread(getMessagesThread);
        th.setDaemon(true);
        th.start();

        System.out.println("\nLet's start!\n");
        System.out.println("'" + privateMessageMarker + "name" + " ...'" + " for private message");
        System.out.println("'" + privateMessageMarker + "room" + " ...'" + " for room message");
        System.out.println("'" + actionMarker + JOIN + " room' for join room");
        System.out.println("'" + actionMarker + LEAVE + " room' for leave room");
        System.out.println("'" + actionMarker + LOGOUT + "' for log out");

        System.out.println("\nEnter your message : ");
        while (true) {
            String text = scanner.nextLine();
            if (text.toLowerCase().equals(actionMarker + LOGOUT)) {
                logout(userName);
                getMessagesThread.stopThread();
                break;
            }

            if (text.toLowerCase().startsWith(actionMarker + JOIN)) {
                String room = text.substring(actionMarker.length() + JOIN.length() + 1);
                joinExitRoom(userName, room, JOIN);
                System.out.print("You joined to '" + room + "'. ");
                System.out.println("For leaving - '" + actionMarker + LEAVE + " " + room + "'");
                continue;
            }

            if (text.toLowerCase().startsWith(actionMarker + LEAVE)) {
                String room = text.substring(actionMarker.length() + LEAVE.length() + 1);
                joinExitRoom(userName, room, LEAVE);
                System.out.println("You leaved '" + room + "'");
                continue;
            }

            Message message = new Message(userName, text);
            int res = message.send(Utils.getURL() + "/chat/add?to=" + message.getTo());

            if (res != 200) { // 200 OK
                System.out.println("HTTP error occurred: " + res);
                return;
            }
        }
    }

    private static void joinExitRoom(String userName, String room, String action) throws IOException {
        final StringBuilder requestLine = new StringBuilder();
        requestLine.append(Utils.getURL()).append("/chat/user");
        requestLine.append("?").append("login=").append(userName);
        requestLine.append("&").append("room=").append(room);
        requestLine.append("&").append("action=").append(action);

        URL get = new URL(requestLine.toString());
        HttpURLConnection conn = (HttpURLConnection) get.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Cookie", sessionId);
        conn.connect();
        conn.getResponseMessage();
    }

    private static void logout(String userName) throws IOException {

        URL get = new URL(Utils.getURL() + "/chat/logout?login=" + userName + "&action=" + LOGOUT);
        HttpURLConnection conn = (HttpURLConnection) get.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Cookie", sessionId);
        conn.connect();

        conn.getHeaderField("session_status");
        System.out.println("'" + userName + "' has been logged out.");

        sessionId = null;
    }

    private static void showAllUsers() throws IOException {
        URL get = new URL(Utils.getURL() + "/chat/users");
        HttpURLConnection conn = (HttpURLConnection) get.openConnection();
        conn.setRequestMethod("GET");

        InputStream is = conn.getInputStream();
        try {
            String result = new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining("\n"));

            System.out.println("All users: \n" + result);
        } finally {
            is.close();
        }
    }

}

package com.savkova.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static com.savkova.chat.client.Utils.*;

public class ConsoleClient {
    private static Scanner scanner = new Scanner(System.in);
    private static String sessionId;

    public static void main(String[] args) {
        System.out.println("Welcome to ChatEE!");
        MenuOptions.show();

        try {
            while (true) {
                int actionNumber = askIdMenuOption();
                switch (actionNumber) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        showAllUsers();
                        break;
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
                continue;
            }
        }
        return actionNumber;
    }

    private static void startChat(String userName) throws IOException {

        startThreads(ALL, userName);

        System.out.println("\nLet's start!\n");

        System.out.println("Enter your message ('" + STOP + "' for log out): ");
        while (true) {
            String text = scanner.nextLine();
            if (text.toLowerCase().equals(STOP)) {
                logout(userName);
                GetMessagesThread.stopThreads(true);
                break;
            }

            String to;
            Message message;
            if ((text.startsWith(privateMessageMarker)) && (text.contains(delimiter))) {
                to = text.substring(1, text.indexOf(delimiter));
                text = text.substring(text.indexOf(delimiter) + 1).trim();
                message = new Message(userName, text, to);
                System.out.println(message);
            } else
                to = ALL;

            message = new Message(userName, text, to);
            int res = message.send(Utils.getURL() + "/chat/add?to=" + to);

            if (res != 200) { // 200 OK
                System.out.println("HTTP error occured: " + res);
                return;
            }
        }
    }

    private static void startThreads(String... values) {
        GetMessagesThread.stopThreads(false);

        for (String to : values) {
            Thread th = new Thread(new GetMessagesThread(to));
            th.setDaemon(true);
            th.start();
        }

    }

    private static void login() throws IOException {

        System.out.print("Enter your login: ");
        String userName = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        boolean succeeded = isLogin(userName, password);
        if (succeeded) {
            startChat(userName);
        } else {
            System.out.println("Couldn't find your account or wrong password.");
        }
    }

    private static boolean isLogin(String userName, String password) throws IOException {
        URL url = new URL(Utils.getURL() + "/chat/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty(LOGIN, userName);
        conn.setRequestProperty(PASS, password);
        sessionId = conn.getHeaderField("Set-Cookie");

        return (conn.getHeaderField(LOGIN).equals("true"))
                && (conn.getHeaderField(PASS).equals("true"));
    }

    private static void createAccount() throws IOException {

        System.out.println("\nFor creating new account ");

        System.out.print("enter your login: ");
        String userName = scanner.nextLine();

        System.out.print("enter your password: ");
        String password = scanner.nextLine();

        boolean succeeded = isAccountCreated(userName, password);
        if (succeeded) {
            System.out.println("New account created - login '" + userName + "'");
        } else {
            System.out.println("That username is taken. Try another.");
        }
    }

    private static boolean isAccountCreated(String userName, String password) throws IOException {

        URL url = new URL(Utils.getURL() + "/chat/signup");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty(LOGIN, userName);
        conn.setRequestProperty(PASS, password);

        return (conn.getHeaderField(ACCOUNT).equals("created"));
    }

    private static void logout(String userName) throws IOException {

        URL get = new URL(Utils.getURL() + "/chat/logout?login=" + userName + "&action=" + STOP);
        HttpURLConnection conn = (HttpURLConnection) get.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Cookie", sessionId.substring(0, sessionId.indexOf(";")));

        conn.getHeaderField("session_status");
        System.out.println("'" + userName + "' has been logged out.");
        sessionId = null;
    }

    private static void showAllUsers() throws IOException {
        URL get = new URL(Utils.getURL() + "/chat/users");
        HttpURLConnection conn = (HttpURLConnection) get.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer result = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();

        System.out.println("All users: " + result.toString());
    }

}

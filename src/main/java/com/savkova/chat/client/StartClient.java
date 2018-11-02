package com.savkova.chat.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class StartClient {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to ChatEE!");
        MenuOptions.show();

        try {
            while (true) {
                int actionNumber = askIdMenuOption();
                switch (actionNumber) {
                    case 1:
                        login();
                        break;
                    case 2:
                        createAccount();
                        break;
                    case 3:
                        logout();
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
        String all = "all";

        startThreads(all, userName);

        System.out.println("\nLet's start!\n");

        System.out.println("Enter your message ('stop' to main menu): ");
        while (true) {
            String text = scanner.nextLine();
            if (text.toLowerCase().equals("stop")) {
                GetThread.stopThreads(true);
                break;
            }

            String privateMessageMarker = "@";
            String delimeter = ":";
            String to;
            if ((text.startsWith(privateMessageMarker)) && (text.contains(delimeter))) {
                to = text.substring(1, text.indexOf(delimeter));
                text = text.substring(text.indexOf(delimeter));
                Message m = new Message(userName, text, to);
                System.out.println(m);
            } else
                to = "all";

            Message m = new Message(userName, text, to);
            int res = m.send(Utils.getURL() + "/chat/add?to=" + to);

            if (res != 200) { // 200 OK
                System.out.println("HTTP error occured: " + res);
                return;
            }
        }
    }

    private static void startThreads(String... values) {
        GetThread.stopThreads(false);

        for (String to : values) {
            Thread th = new Thread(new GetThread(to));
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
        conn.setRequestProperty("login", userName);
        conn.setRequestProperty("password", password);

        return (conn.getHeaderField("login").equals("true"))
                && (conn.getHeaderField("password").equals("true"));
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
        conn.setRequestProperty("login", userName);
        conn.setRequestProperty("password", password);

        return (conn.getHeaderField("account").equals("created"));
    }

    private static void logout() {
        // TODO: close session
        //TODO logout
    }

}
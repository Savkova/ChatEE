package com.savkova.chat.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to ChatEE!");
        MenuOptions.show();

        try {
            int actionNumber = askIdMenuOption();

            switch (actionNumber) {
                case 1:
                    boolean isLogin = login();
                    if (!isLogin)
                        createAccount();
                    break;
                case 2:
                    createAccount();
                    login();
                    break;
                case 3:
                    System.out.println("Quit");
                    System.exit(0);
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

    private static boolean login() throws IOException {
        System.out.print("Enter your login: ");
        String userName = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        boolean succeeded = verifyUser(userName, password);
        if (succeeded) {
            startChat(userName);
            return true;
        } else {
            System.out.println("Couldn't find your account.");
            return false;
        }
    }

    private static void startChat(String userName) throws IOException {
        Thread th = new Thread(new GetThread());
        th.setDaemon(true);
        th.start();

        System.out.println("Enter your message: ");
        while (true) {
            String text = scanner.nextLine();
            if (text.toLowerCase().equals("quit")) {
                System.exit(0);
            }

            Message m = new Message(userName, text);
            int res = m.send(Utils.getURL() + "/chat/add");

            if (res != 200) { // 200 OK
                System.out.println("HTTP error occured: " + res);
                return;
            }
        }
    }

    private static boolean verifyUser(String userName, String password) throws IOException {
        URL url = new URL(Utils.getURL() + "/chat/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("login", userName);
        conn.setRequestProperty("password", password);

        return (conn.getHeaderField("login").equals("true"))
                && (conn.getHeaderField("password").equals("true"));
    }

    private static void createAccount() throws IOException {
        // TODO: add authorization for new users
        System.out.println("Created new account.");
        login();
    }
}

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
                        // TODO: close session
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
        String to = "all";

        Thread commonThead = new Thread(new GetThread(to));
        commonThead.setDaemon(true);
        commonThead.start();

        System.out.println("\nLet's start!\n");

        System.out.println("Enter your message ('stop' to main menu): ");
        while (true) {
            String text = scanner.nextLine();
            if (text.toLowerCase().equals("stop")) {
                break;
            }

            if ((text.startsWith("@")) && (text.contains(":"))) {
                to = text.substring(1, text.indexOf(":"));
                text = text.substring(text.indexOf(":"));
/*
                Thread privateThread = new Thread(new GetThread(to));
                privateThread.setDaemon(true);
                privateThread.start();*/
            }

            Message m = new Message(userName, text, to);
            int res = m.send(Utils.getURL() + "/chat/add?to=" + to);

            if (res != 200) { // 200 OK
                System.out.println("HTTP error occured: " + res);
                return;
            }
        }
    }

    private static boolean login() throws IOException {
        System.out.print("Enter your login: ");
        String userName = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        boolean succeeded = isLogin(userName, password);
        if (succeeded) {
            startChat(userName);
            return true;
        } else {
            System.out.println("Couldn't find your account or wrong password.");
            return false;
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

    private static boolean createAccount() throws IOException {

        System.out.println("\nFor creating new account ");

        System.out.print("enter your login: ");
        String userName = scanner.nextLine();

        System.out.print("enter your password: ");
        String password = scanner.nextLine();

        boolean succeeded = isAccountCreated(userName, password);
        if (succeeded) {
            System.out.println("New account created - login '" + userName + "'");
            return true;
        } else {
            System.out.println("That username is taken. Try another.");
            return false;
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
        //TODO logout
    }

}

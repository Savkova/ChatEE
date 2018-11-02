package com.savkova.chat.server.util;

import com.savkova.chat.server.entities.User;
import com.savkova.chat.server.storage.UsersStorage;

public class VerifyUser {

    public static boolean isLoginAlreadyUsed(String login) {

        User user = UsersStorage.getInstance().getUser(login);

        return (user != null);
    }

    public static boolean isLoginPasswordCorrect(String login, String password) {

        User user = UsersStorage.getInstance().getUser(login);
        if (user != null) {
            return (user.getName().equals(login) && user.getPassword().equals(password));
        }

        return false;
    }

}

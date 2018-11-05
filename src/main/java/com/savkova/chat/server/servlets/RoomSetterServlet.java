package com.savkova.chat.server.servlets;

import com.savkova.chat.server.entities.User;
import com.savkova.chat.server.storage.MessageStorage;
import com.savkova.chat.server.storage.UsersStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.savkova.chat.server.util.Properties.*;

@WebServlet(name = "RoomSetter", urlPatterns = "/user")
public class RoomSetterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter(LOGIN);
        String room = request.getParameter(ROOM);
        String action = request.getParameter(ACTION);

        MessageStorage.getInstance().addRoom(room);

        User user = UsersStorage.getInstance().getUser(login);

        if (action.equals(JOIN))
            user.addRoom(room);

        if (action.equals(LEAVE))
            user.removeRoom(room);
    }
}

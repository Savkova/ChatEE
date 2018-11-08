package com.savkova.chat.server.servlets;

import com.savkova.chat.server.entities.User;
import com.savkova.chat.server.storage.MessageStorage;
import com.savkova.chat.server.storage.UsersStorage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GetList", urlPatterns = "/get")
public class GetMessageListServlet extends HttpServlet {

    private MessageStorage storage = MessageStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userName = req.getHeader("user");

        User user = UsersStorage.getInstance().getUser(userName);
        Set<String> rooms = user.getRooms();

        resp.setContentType("application/json");

        for (String to : rooms) {
            String json = storage.toJSON(to);
            if (!json.equals("")) {
                PrintWriter pw = resp.getWriter();
                pw.print(json);
            }
        }
    }
}

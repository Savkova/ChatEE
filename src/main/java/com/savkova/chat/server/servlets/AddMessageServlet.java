package com.savkova.chat.server.servlets;

import com.savkova.chat.server.entities.*;
import com.savkova.chat.server.storage.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Add", urlPatterns = "/add")
public class AddMessageServlet extends HttpServlet {

    private MessageStorage messageStorage = MessageStorage.getInstance();
    private UsersStorage usersStorage = UsersStorage.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] buf = requestBodyToArray(request);
        String bufStr = new String(buf, StandardCharsets.UTF_8);

        Message msg = Message.fromJSON(bufStr);

        String from = request.getParameter("from");
        String to = request.getParameter("to");

        if (msg != null) {
            List<User> users = usersStorage.getAllUsers();
            Set<String> rooms;
            for (User user : users) {
                rooms = user.getRooms();
                String userName = user.getName();
                if (rooms.contains(to) && !from.equals(userName))
                    messageStorage.add(msg, userName);
            }
        } else
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private byte[] requestBodyToArray(HttpServletRequest req) throws IOException {
        InputStream is = req.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}

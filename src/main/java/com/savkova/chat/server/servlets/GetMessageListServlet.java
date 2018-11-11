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
import javax.servlet.http.HttpSession;

import static com.savkova.chat.server.util.Constants.LOGIN;

@WebServlet(name = "GetList", urlPatterns = "/get")
public class GetMessageListServlet extends HttpServlet {

    private MessageStorage storage = MessageStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getHeader("user");
        User user = UsersStorage.getInstance().getUser(userName);

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(LOGIN).equals(userName)) {
            Set<String> rooms = user.getRooms();

            response.setContentType("application/json");

            for (String to : rooms) {
                String json = storage.toJSON(to);
                if (!json.equals("")) {
                    PrintWriter pw = response.getWriter();
                    pw.print(json);
                }
            }
        } else
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }
}

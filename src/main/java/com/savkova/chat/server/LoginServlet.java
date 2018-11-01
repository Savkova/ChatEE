package com.savkova.chat.server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getHeader("login");
        String password = request.getHeader("password");

        User user = Users.getInstance().getUser(login);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user_login", login);
                response.setHeader("session", "true");
            } else {
                response.setHeader("password", "true");
            }
        } else
            response.setHeader("session", "false");
    }
}

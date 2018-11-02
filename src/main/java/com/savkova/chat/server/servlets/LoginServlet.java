package com.savkova.chat.server.servlets;

import com.savkova.chat.server.util.VerifyUser;

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

        if (VerifyUser.isLoginAlreadyUsed(login)) {
            if (VerifyUser.isLoginPasswordCorrect(login, password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user_login", login);
                response.setHeader("login", "true");
                response.setHeader("password", "true");
            } else
                response.setHeader("password", "false");
        } else
            response.setHeader("login", "false");
    }

}

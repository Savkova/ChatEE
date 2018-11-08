package com.savkova.chat.server.servlets;

import com.savkova.chat.server.entities.User;
import com.savkova.chat.server.storage.UsersStorage;
import com.savkova.chat.server.util.VerifyUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

import static com.savkova.chat.server.util.Constants.*;

@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getHeader(LOGIN);
        String password = request.getHeader(PASS);

        if (VerifyUser.isLoginAlreadyUsed(login)) {
            if (VerifyUser.isLoginPasswordCorrect(login, password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute(LOGIN, login);
                response.setHeader(LOGIN, "true");
                response.setHeader(PASS, "true");

                User user = UsersStorage.getInstance().getUser(login);
                user.setStatus(true);
            } else {
                response.setHeader(PASS, "false");
                response.setStatus(400);
            }
        } else {
            response.setHeader(LOGIN, "false");
        }
        response.setStatus(404);
    }

}

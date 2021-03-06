package com.savkova.chat.server.servlets;

import com.savkova.chat.server.entities.User;
import com.savkova.chat.server.storage.UsersStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.savkova.chat.server.util.Constants.*;

@WebServlet(name = "Logout", urlPatterns = "/logout")
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter(LOGIN);
        String action = request.getParameter(ACTION);

        HttpSession session;
        if (action.equals(LOGOUT)) {
            session = request.getSession(false);
            if (session != null) session.invalidate();
            response.setHeader("session_status", "'" + login + "' session has been destroyed.");

            User user = UsersStorage.getInstance().getUser(login);
            user.setStatus(false);

            return;
        }

        session = request.getSession(false);
        if (session == null) {
            response.setHeader("session_status", "No active session.");
        }
    }

}

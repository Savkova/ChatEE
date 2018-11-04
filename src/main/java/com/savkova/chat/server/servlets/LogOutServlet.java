package com.savkova.chat.server.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.savkova.chat.server.util.Properties.*;

@WebServlet(name = "Logout", urlPatterns = "/logout")
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter(LOGIN);
        String action = request.getParameter(ACTION);

        HttpSession session;
        if (action.equals(STOP)) {
            session = request.getSession(false);
            if (session != null) session.invalidate();
            response.setHeader("session_status", "'" + login + "' session have been destroyed.");
            return;
        }

        session = request.getSession(false);
        if (session == null) {
            response.setHeader("session_status", "No active session.");
        }
    }

}

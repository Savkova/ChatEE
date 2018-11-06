package com.savkova.chat.server.servlets;

import com.savkova.chat.server.entities.User;
import com.savkova.chat.server.storage.UsersStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetAllUsers", urlPatterns = "/users")
public class GetAllUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<User> users = UsersStorage.getInstance().getAllUsers();

        List<String> usersInfo = new ArrayList<>();
        for (User user : users) {
            usersInfo.add(user.toString());
        }

        PrintWriter pw = response.getWriter();
        for (String info : usersInfo) {
            pw.println(info);
        }

    }

}

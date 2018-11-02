package com.savkova.chat.server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignUp", urlPatterns = "/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getHeader("login");

        if (VerifyUser.isLoginAlreadyUsed(login)) {
            response.setHeader("account", "already exist");
        } else {
            String password = request.getHeader("password");
            Users.getInstance().add(new User(login, password));
            response.setHeader("account", "created");
        }
    }

}

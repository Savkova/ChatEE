package com.savkova.chat.server.servlets;

import com.savkova.chat.server.entities.User;
import com.savkova.chat.server.storage.UsersStorage;
import com.savkova.chat.server.util.VerifyUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.savkova.chat.server.util.Constants.*;

@WebServlet(name = "SignUp", urlPatterns = "/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getHeader(LOGIN);

        if (VerifyUser.isLoginAlreadyUsed(login)) {
            response.setHeader(ACCOUNT, "already exist");
        } else {
            String password = request.getHeader(PASS);
            UsersStorage.getInstance().add(new User(login, password));
            response.setHeader(ACCOUNT, "created");
        }
    }

}

package com.savkova.chat.server.servlets;

import com.savkova.chat.server.storage.MessageStorage;
import com.savkova.chat.server.util.Constants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "GetList", urlPatterns = "/get")
public class GetMessageListServlet extends HttpServlet {

    private MessageStorage storage = MessageStorage.getInstance();
    private static final int waitCounter = 30;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getHeader("user");
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(Constants.LOGIN).equals(userName)) {

            response.setContentType("application/json");

            // if there are no messages - keeps connection
            int counter = 0;
            String json;
            while ((json = storage.toJSON(userName)).equals("") && counter < waitCounter) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter++;
            }

            response.getOutputStream().write(json.getBytes("UTF-8"));

            if (!json.equals(""))
                storage.clearMessageList(userName);
        }
    }
}

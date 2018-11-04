package com.savkova.chat.server.servlets;

import com.savkova.chat.server.storage.MessageStorage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GetList", urlPatterns = "/get")
public class GetMessageListServlet extends HttpServlet {

	private MessageStorage storage = MessageStorage.getInstance();

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String fromStr = req.getParameter("fromN");
		String to = req.getParameter("to");
		int fromN = 0;
		try {
			fromN = Integer.parseInt(fromStr);
			if (fromN < 0) fromN = 0;
		} catch (Exception ex) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
		}

		resp.setContentType("application/json");

		String json = storage.toJSON(fromN, to);
		if (json != null) {
			PrintWriter pw = resp.getWriter();
			pw.print(json);
		}
	}
}

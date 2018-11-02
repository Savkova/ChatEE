package com.savkova.chat.server.servlets;

import com.savkova.chat.server.entities.Message;
import com.savkova.chat.server.storage.MessageStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Add", urlPatterns = "/add")
public class AddServlet extends HttpServlet {

	private MessageStorage messageStorage = MessageStorage.getInstance();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		byte[] buf = requestBodyToArray(req);
        String bufStr = new String(buf, StandardCharsets.UTF_8);

        String to = req.getParameter("to");

		Message msg = Message.fromJSON(bufStr);
		if (msg != null)
			messageStorage.add(msg, to);
		else
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	private byte[] requestBodyToArray(HttpServletRequest req) throws IOException {
        InputStream is = req.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}

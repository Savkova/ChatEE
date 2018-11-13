package com.savkova.chat.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetMessagesThread implements Runnable {
    private final Gson gson;
    private String userName;
    private boolean isStop;

    public GetMessagesThread(String userName) {
        this.gson = new GsonBuilder().create();
        this.userName = userName;
        this.isStop = false;
    }

    @Override
    public void run() {
        try {
            while (!isStop) {
                URL url = new URL(Utils.getURL() + "/chat/get");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("user", userName);
                conn.setRequestProperty("Cookie", ConsoleClient.sessionId);
                conn.setRequestProperty("Connection", "Keep-Alive");

                InputStream is = conn.getInputStream();

                byte[] buf = requestBodyToArray(is);
                String strBuf = new String(buf, StandardCharsets.UTF_8);

                JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                if (list != null) {
                    for (Message m : list.getList()) {
                        System.out.println(m);
                    }
                }
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void stopThread() {
        this.isStop = true;
    }

    private byte[] requestBodyToArray(InputStream is) throws IOException {
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

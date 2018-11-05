package com.savkova.chat.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class GetMessagesThread implements Runnable {
    private final Gson gson;
    private Date lastReadDate;
    private String userName;
    private static boolean isStop;

    public GetMessagesThread(String userName) {
        gson = new GsonBuilder().create();
        this.userName = userName;
        this.lastReadDate = new Date(1541030400);
    }

    @Override
    public void run() {
        try {
            while (!isStop) {
                URL url = new URL(Utils.getURL() + "/chat/get");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("user", userName);

                InputStream is = conn.getInputStream();

                Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                JsonMessages json;
                if (reader.ready())
                    try {
                        JsonStreamParser parser = new JsonStreamParser(reader);
                        while (parser.hasNext()) {
                            JsonElement e = parser.next();
                            if (e.isJsonObject()) {
                                json = gson.fromJson(e, JsonMessages.class);

                                for (Message m : json.getList()) {
                                    if (m.getDate().after(lastReadDate)) {
                                        System.out.println(m);
                                        lastReadDate = m.getDate();
                                    }
                                }
                            }
                        }
                        reader.close();
                    } finally {
                        is.close();
                    }

                Thread.sleep(500);
            }
        } catch (Exception ex) {
            Thread.currentThread().interrupt();
            ex.printStackTrace();
        }
    }

    public static void stopThread(boolean marker) {
        isStop = marker;
    }
}

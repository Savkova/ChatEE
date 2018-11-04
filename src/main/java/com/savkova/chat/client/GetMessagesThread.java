package com.savkova.chat.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetMessagesThread implements Runnable {
    private final Gson gson;
    private int n;
    private String to;

    private static boolean isStop;

    public GetMessagesThread(String to) {
        gson = new GsonBuilder().create();
        this.to = to;
    }

    @Override
    public void run() {
        try {
            while ( ! isStop) {
                URL url = new URL(Utils.getURL() + "/chat/get?fromN=" + n + "&to=" + to);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                InputStream is = conn.getInputStream();
                try {
                    byte[] buf = requestBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                    if (list != null) {
                        for (Message m : list.getList()) {
                            System.out.println(m);
                            n++;
                        }
                    }
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

    public static void stopThreads(boolean marker){
        isStop = marker;
    }
}

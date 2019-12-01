package com.company;

import com.company.models.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetThread implements Runnable {
    private final Gson gson;
    private int n;
    private String endpoint;
    private String prefix;

    public GetThread(String endpoint) {
        gson = new GsonBuilder().create();
        this.endpoint = endpoint;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                URL url = new URL(endpoint + n);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                InputStream is = http.getInputStream();

                try {
                    byte[] buf = Utils.requestBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                    if (list != null) {
                        for (Message m : list.getList()) {
                            if (endpoint.equals(Features.GET_GLOBAL_MSG_URL)) {
                                prefix = "Global chat";
                            } else {
                                prefix = "Private message";
                            }
                            System.out.println(prefix + " : " + m);
                            n++;
                        }
                    }
                } finally {
                    is.close();
                }

                Thread.sleep(500);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

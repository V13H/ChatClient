package com.company;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;
    public static final String PRIVATE_MSG_URL = getURL() + "/privateMessages";

    public static String getURL() {
        return URL + ":" + PORT;
    }
    public static byte[] requestBodyToArray(InputStream is) throws IOException {
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

package com.company;

import com.company.models.UsersList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import  com.company.models.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String login;
        String password;
        try {
            boolean actionPerformedSuccessfully = false;
            System.out.println("Welcome to our chat");
            do {
                System.out.println("Enter your login: ");
                login = scanner.nextLine();

                System.out.println("Enter your password: ");
                password = scanner.nextLine();

                System.out.println("Choose action: 1)Create account; 2)Sign in");
                String action = scanner.nextLine();

                URL url = new URL(Utils.getURL() + "/auth?login=" + login + "&password=" + password + "&action=" + action);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("login", login);
                connection.setRequestProperty("password", password);
                connection.setDoOutput(true);
                InputStream inputStream = connection.getInputStream();
                byte[] bytes = Utils.requestBodyToArray(inputStream);
                String s = new String(bytes, StandardCharsets.UTF_8);
                System.out.println(s);
                if (s.contains("successfully")) {
                    actionPerformedSuccessfully = true;
                }
            } while (!actionPerformedSuccessfully);

            Thread th = new Thread(new GetThread(Features.GET_GLOBAL_MSG_URL));
            th.setDaemon(true);
            th.start();
            Thread th2 = new Thread(new GetThread(Features.GET_PRIVATE_MSG_URL+login+"&from="));
            th2.setDaemon(true);
            th2.start();
            Features.initMainMenu(login,scanner);



        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }

}

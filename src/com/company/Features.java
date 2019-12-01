package com.company;

import com.company.models.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import com.company.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Features {
    public static final String GLOBAL_MSG_URL = Utils.getURL()+"/add";
    public static final String UPDATE_STATUS_URL = Utils.getURL()+"/updateStatus";
    public static final String GET_PRIVATE_MSG_URL = Utils.getURL()+"/getPrivate?user=";
    public static final String ADD_PRIVATE_MSG_URL = Utils.getURL()+"/addPrivate";
    public static final String GET_GLOBAL_MSG_URL = Utils.getURL() + "/get?from=";
    public static final String CREATE_CHAT_GROUP_URL = Utils.getURL() + "/createChatGroup";
    public static final String GET_ALL_CHAT_GROUPS_URL = Utils.getURL() + "/getChatGroups";


    private static void sendMessage(String login, String to, String url, Scanner scanner) throws IOException {
        System.out.println("Enter your message: ");
        while (true) {
            int res;
            String text = scanner.nextLine();
            if (text.isEmpty()||text.equalsIgnoreCase("e")) break;
            if(text.equalsIgnoreCase("b")){
                initMainMenu(login, scanner);
                break;
            }
            Message m = new Message(login, text);
            if(to == null||to.isEmpty()){
                m.setTo(Message.TO_GLOBAL_CHAT);
                res = m.send(url);
            }else {
                m.setTo(to);
                res = m.send(url);
            }

            if (res != 200) { // 200 OK
                System.out.println("HTTP error occured: " + res);
                return;
            }
        }
    }
    private static UsersList getUsersList() throws IOException {
        URL url = new URL(Utils.getURL() + "/userslist");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] buf = Utils.requestBodyToArray(inputStream);
        String strBuf = new String(buf, StandardCharsets.UTF_8);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(strBuf, UsersList.class);
    }
    static void initMainMenu(String login, Scanner scanner) throws IOException {
        System.out.println("Choose action:");
        System.out.println("1.Enter to the global chat");
        System.out.println("2.Send private message");
        System.out.println("3.Create chat group");
        System.out.println("4.Get all users list");
        System.out.println("5.Check user`s status");
        System.out.println("6.Update status");
        System.out.println("7.Create chat group");
        System.out.println("8.Get chat groups list");

        String action = scanner.nextLine();
        switch (action) {
            case "1":
                Features.sendMessage(login,null,Features.GLOBAL_MSG_URL,scanner);
                break;
            case "2":
                System.out.println("Send message to : ");
                String to = scanner.nextLine();
                Features.sendMessage(login,to,Features.ADD_PRIVATE_MSG_URL,scanner);
                break;
            case "3":
                break;
            case "4":
                UsersList usersList = Features.getUsersList();
                ConcurrentHashMap<String, User> usersMap = usersList.getUsersMap();
                for (String userName : usersMap.keySet()) {
                    System.out.println(userName+"   "+usersMap.get(userName).toString());
                }
                initMainMenu(login, scanner);
                break;
            case "5":
                System.out.println("User to check: ");
                String userToCheck = scanner.nextLine();
                checkUserStatus(userToCheck);
                initMainMenu(login, scanner);
                break;
            case "6" :
                System.out.println("New status: ");
                String newStatus = scanner.nextLine();
                setStatus(newStatus,login);
                initMainMenu(login, scanner);
                break;
            case "7":
                System.out.println("Enter group title: ");
                String groupTitle = scanner.nextLine();
                createChatGroup(groupTitle,login);
                initMainMenu(login, scanner);
                break;
            case "8":
                System.out.println("Groups list: ");
                System.out.println(getAllGroups().getGroupsMap().values().toString());
                break;

        }
    }
    private static void checkUserStatus(String userName) throws IOException {
        System.out.println(getUsersList().getUsersMap().get(userName).getStatus());
    }
    private static void setStatus(String newStatus,String currentUserLogin) throws IOException {
        if(newStatus.equalsIgnoreCase("afk")||newStatus.equalsIgnoreCase("happy")){
            URL url = new URL(UPDATE_STATUS_URL+"?newStatus="+newStatus+"&currentUser="+currentUserLogin);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("newStatus",newStatus);
            connection.setRequestProperty("currentUser",currentUserLogin);
            System.out.println(connection.getResponseCode());

        }
    }
    private static void createChatGroup(String groupTitle,String login) throws IOException {
        User currentUser = getUsersList().getUsersMap().get(login);
        ChatGroup chatGroup = new ChatGroup(currentUser,groupTitle);
        chatGroup.getUsers().add(currentUser);
        URL url = new URL(CREATE_CHAT_GROUP_URL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(chatGroup,ChatGroup.class);
        OutputStream os = connection.getOutputStream();
        os.write(json.getBytes(StandardCharsets.UTF_8));
        System.out.println(connection.getResponseCode());
    }
    private static GroupsList getAllGroups() throws IOException {
        URL url = new URL(GET_ALL_CHAT_GROUPS_URL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        Gson gson = new GsonBuilder().create();
        InputStream inputStream = connection.getInputStream();
        byte[] bytes = Utils.requestBodyToArray(inputStream);
        String buf = new String(bytes,StandardCharsets.UTF_8);
        return gson.fromJson(buf,GroupsList.class);
    }
}

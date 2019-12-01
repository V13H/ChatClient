package com.company.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ConcurrentHashMap;

public class UsersList {
    public final static UsersList users = new UsersList();
    private final ConcurrentHashMap<String,User> usersMap = new ConcurrentHashMap<>();

    public static UsersList getInstance(){
        return users;
    }
    public void addUser(String login,String password){
        if(!usersMap.containsKey(login)){
            usersMap.put(login,new User(login, password));
        }
    }
    public boolean isLoginFree(String login){
        return !usersMap.containsKey(login);
    }
    public boolean isPasswordEnteredCorrectly(String login,String password){
        boolean result = false;
        if(usersMap.containsKey(login)){
            result = password.equals(usersMap.get(login));
        }
        return result;
    }
    public synchronized String toJSON(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(users);
    }
    @Override
    public String toString() {
        return "UsersList{" +
                "usersMap=" + usersMap +
                '}';
    }

    public ConcurrentHashMap<String, User> getUsersMap() {
        return usersMap;
    }
}

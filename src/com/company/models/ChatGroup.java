package com.company.models;

import java.util.List;
import java.util.UUID;

public class ChatGroup {
    private User admin;
    private List<User> users;
    private String title;
    private UUID groupID = UUID.randomUUID();

    public ChatGroup(User admin, String title) {
        this.admin = admin;
        this.title = title;
    }


    public User getAdmin() {
        return admin;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getTitle() {
        return title;
    }

    public UUID getGroupID() {
        return groupID;
    }

    @Override
    public String toString() {
        return "ChatGroup{" +
                "admin=" + admin +
                ", users=" + users +
                ", title='" + title + '\'' +
                '}';
    }
}

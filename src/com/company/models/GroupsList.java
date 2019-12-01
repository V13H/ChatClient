package com.company.models;

import java.util.concurrent.ConcurrentHashMap;

public class GroupsList {
    private final static GroupsList groupsList = new GroupsList();
    private final ConcurrentHashMap<String,ChatGroup> groupsMap = new ConcurrentHashMap<>();

    private GroupsList() {
    }
    public static GroupsList getInstance(){
        return groupsList;
    }
    public void add(String user,ChatGroup group){
        groupsMap.put(user, group);
    }

    public ConcurrentHashMap<String, ChatGroup> getGroupsMap() {
        return groupsMap;
    }
}

package com.example.scheduleandaccountbook;

public class Personal_User_DB {

    private String Uid; //firebase Uid(고유 토큰정보, 회원의 키값)
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Personal_User_DB(String groupName) {
        this.groupName = groupName;

    }
}



package com.example.scheduleandaccountbook;

import java.util.Map;

public class Public_User_DB {
    private String groupName;
    private String creatorUid;
    private String inviteCode;
    private Map<String, Boolean> members;
    public Public_User_DB() {}

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(String creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Public_User_DB(String groupName, String creatorUid, String inviteCode) {
        this.groupName = groupName;
        this.creatorUid = creatorUid;
        this.inviteCode = inviteCode;
        this.members = members;
    }

}

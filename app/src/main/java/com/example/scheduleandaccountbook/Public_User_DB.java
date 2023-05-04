package com.example.scheduleandaccountbook;

import java.util.Map;

public class Public_User_DB {
    private String groupName;
    private String creatorUid;
    private String inviteCode;
    private String members;

    public String getMembers() { return members; }

    public void setMembers(String members) { this.members = members; }
//    public Public_User_DB() {}

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


}

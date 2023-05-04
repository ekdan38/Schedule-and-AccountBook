package com.example.scheduleandaccountbook;

//사용자 계정 정보 모델 클래스
public class UserAccount
{
    private String Uid; //firebase Uid(고유 토큰정보, 회원의 키값)
    private String Id;//id
    private String Pwd;//pwd
    private String GroupId;//가입하는 그룹의 고유번호

    public String getGroupId() { return GroupId; }

    public void setGroupId(String groupId) { GroupId = groupId; }


    public UserAccount(){}

    public String getUid() {
        return Uid;
    }

    public void setUid(String idToken) {
        this.Uid = idToken;
    }

    public String getId(){return Id;}
    public void setId(String Id)
    {
        this.Id = Id;
    }

    public String getPwd()
    {
        return Pwd;
    }

    public void setPwd(String Pwd)
    {
        this.Pwd = Pwd;
    }
}

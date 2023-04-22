package com.example.scheduleandaccountbook;

//사용자 계정 정보 모델 클래스
public class UserAccount
{
    private String IdToken; //firebase Uid(고유 토큰정보, 회원의 키값)
    private String Id;//id
    private String Pwd;//pwd

    public UserAccount(){}

    public String getIdToken() {
        return IdToken;
    }

    public void setIdToken(String idToken) {
        this.IdToken = idToken;
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

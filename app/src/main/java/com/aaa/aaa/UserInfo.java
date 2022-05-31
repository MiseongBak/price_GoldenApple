package com.aaa.aaa;

public class UserInfo {

    public String name;
    public int phone_number;
    public String uid;
    public String profile_pic;

    public UserInfo(String name, int phone_number, String uid, String profile_pic) {
        this.name = name;
        this.phone_number = phone_number;
        this.uid = uid;
        this.profile_pic = profile_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}

package com.example.myapplication;


import android.net.Uri;

public class Profile {

    // private String id;
    private String name;
    private String phone;
    private String email;
    private Uri avatar;

    public void Profile(){}

    public void setProfile( String name, String phone, String email, Uri avatar){
        //  this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
    }

    //  public String getId() {return id;}

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Uri getAvatar() {return avatar;}
}

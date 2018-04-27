package com.example.myapplication;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String phone;
    public double latitude;
    public double longitude;

    User(String name, String email, String phone, double latitude, double longitude) {
        this.phone=phone;
        this.email=email;
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public User(){

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }
}

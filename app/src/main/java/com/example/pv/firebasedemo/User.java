package com.example.pv.firebasedemo;

/**
 * Created by PV on 1/9/2018.
 */

public class User  {
    String userId;
    String userName;
    String userAddress;
    String userPhoneNumber;

    public User(){

    }

    public User(String userId, String userName, String userAddress, String userPhoneNumber) {
        this.userId = userId;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserId() { return userId; }

    public String getUserName() {
        return userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }


}


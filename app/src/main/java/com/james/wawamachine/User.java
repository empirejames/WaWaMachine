package com.james.wawamachine;

/**
 * Created by 101716 on 2018/6/6.
 */

public class User {
    private String email;
    private String location;
    private String name;
    private String price;
    private String imgName;
    public User() {
        // Needed for Firebase
    }
    public User(String email, String name, String location,  String price, String imgName) {
       this.email = email;
        this.name = name;
        this.location = location;
        this.price = price;
        this.imgName = imgName;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        location = location;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        price = price;
    }
    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        imgName = imgName;
    }
}

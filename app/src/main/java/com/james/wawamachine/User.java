package com.james.wawamachine;

/**
 * Created by 101716 on 2018/6/6.
 */

public class  User {
    private String email;
    private String location;
    private String name;
    private String price;
    private String imgName;
    private String longtitude; //經度
    private String latitude; //緯度
    private String lineid;
    private String contanter;

    public User() {
        // Needed for Firebase
    }
    public User(String email, String name, String location,  String price, String imgName, String longtitude, String latitude, String lineid,String contanter) {
       this.email = email;
        this.name = name;
        this.location = location;
        this.price = price;
        this.imgName = imgName;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.lineid = lineid;
        this.contanter = contanter;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
    public String getLongtitude (){
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLineID() {
        return lineid;
    }

    public void setLineID(String lineid) {
        this.lineid = lineid;
    }

    public String getContanter() {
        return contanter;
    }

    public void setContanter(String contanter) {
        this.contanter = contanter;
    }
}

package com.example.lenovo.mynewfarm.Model;

public class Sellers {

    private String phone;
    private String password;
    private String email;
    private String address;
    public Sellers(String name, String phone, String password, String image, String address) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.address = address;
    }

    public Sellers()
    {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }






}

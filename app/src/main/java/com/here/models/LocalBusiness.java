package com.here.models;

public class LocalBusiness {
    private String type;
    private String address;
    private String name;
    private String contactNumber;
    private String url;

    public LocalBusiness(String type, String address, String name, String contactNumber, String url) {
        this.type = type;
        this.address = address;
        this.name = name;
        this.contactNumber = contactNumber;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
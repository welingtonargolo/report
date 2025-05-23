package com.example.report.data.models;

public class Problem {
    private long id;
    private long categoryId;
    private long userId;
    private String description;
    private byte[] photo;
    private double latitude;
    private double longitude;
    private String datetime;
    private String status;
    private String categoryName; // Added for UI display

    public Problem() {
    }

    public Problem(long categoryId, long userId, String description, byte[] photo, 
                  double latitude, double longitude, String datetime, String status) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.description = description;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.datetime = datetime;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

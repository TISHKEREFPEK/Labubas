package com.example.labubas;

public class RequestModel {
    int id;
    String ownerName;
    String phone;
    String carModel;
    String issueOrColor;
    String date;
    String time;
    String status;
    String type;

    public RequestModel(int id, String ownerName, String phone, String carModel, String issueOrColor, String date, String time, String status, String type) {
        this.id = id;
        this.ownerName = ownerName;
        this.phone = phone;
        this.carModel = carModel;
        this.issueOrColor = issueOrColor;
        this.date = date;
        this.time = time;
        this.status = status;
        this.type = type;
    }

    public int getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public String getPhone() { return phone; }
    public String getCarModel() { return carModel; }
    public String getIssueOrColor() { return issueOrColor; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
    public String getType() { return type; }

    public String getDescriptionForList() {
        return ownerName + " - " + carModel;
    }
}
package com.example.spendingmanager;

public class Activities {
    String name;
    int type;
    String date;
    String time;
    int amount;
    String ofUser;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getOfUser() {
        return ofUser;
    }

    public void setOfUser(String ofUser) {
        this.ofUser = ofUser;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }



    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getAmount() {
        return amount;
    }


    public Activities(){

    }

    public Activities(String name, int type, String date, String time, int amount, String ofUser) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.ofUser = ofUser;
    }


}

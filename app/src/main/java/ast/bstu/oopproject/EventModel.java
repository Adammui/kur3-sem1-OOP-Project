package ast.bstu.oopproject;

import java.util.Date;

public class EventModel {

    private int id;
    private String title;
    private String imguri;
    private String category;
    private String address;
    private double latitude;
    private double longitude;
    private String price;
    private String date;
    private String note;
    private int priority;
    //title , category, address, latitude, longitude, price, date, note, priority

    public EventModel(String title, String imguri, String category, String address, double latitude, double longitude, String price, String date, String note, int priority) {
        this.title = title;
        this.imguri= imguri;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.date = date;
        this.note = note;
        this.priority = priority;
    }

    public EventModel() {
    }

    public int getId() {
        return id;
    }

    public String title() {
        return title;
    }

    public String category() {
        return category;
    }
    public String imguri() {return imguri;}

    public String address() {
        return address;
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

    public String price() {
        return price;
    }

    public String date() {
        return date;
    }

    public String note() {
        return note;
    }

    public int priority() {
        return priority;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

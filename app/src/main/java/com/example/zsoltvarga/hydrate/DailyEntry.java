package com.example.zsoltvarga.hydrate;

public class DailyEntry {
    private int _id;
    private String _date;
    private int _percentage;

    public DailyEntry(String date, int percentage) {
        this._date = date;
        this._percentage = percentage;
    }
    public DailyEntry(){}

    public void set_id(int id) {
        this._id = id;
    }

    public void set_date(String date) {
        this._date = date;
    }

    public void set_percentage(int percentage) {
        this._percentage = percentage;
    }

    public int get_id() {
        return _id;
    }

    public String get_date() {
        return _date;
    }

    public int get_percentage() {
        return _percentage;
    }
}

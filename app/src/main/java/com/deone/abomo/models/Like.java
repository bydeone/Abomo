package com.deone.abomo.models;

public class Like {
    private String lid;
    private String ldate;

    public Like() {
    }

    public Like(String lid, String ldate) {
        this.lid = lid;
        this.ldate = ldate;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getLdate() {
        return ldate;
    }

    public void setLdate(String ldate) {
        this.ldate = ldate;
    }
}

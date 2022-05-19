package com.deone.abomo.models;

public class Signale {
    private String sid;
    private String sdate;

    public Signale() {
    }

    public Signale(String sid, String sdate) {
        this.sid = sid;
        this.sdate = sdate;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }
}

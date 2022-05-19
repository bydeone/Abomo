package com.deone.abomo.models;

public class Favorite {
    private String fid;
    private String fdate;

    public Favorite() {
    }

    public Favorite(String fid, String fdate) {
        this.fid = fid;
        this.fdate = fdate;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFdate() {
        return fdate;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }
}

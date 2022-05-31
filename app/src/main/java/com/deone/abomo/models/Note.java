package com.deone.abomo.models;

public class Note {
    private String nid;
    private String nnote;
    private String uid;
    private String unoms;
    private String uavatar;
    private String ndate;

    public Note() {
    }

    public Note(String nid, String nnote, String uid, String unoms, String uavatar, String ndate) {
        this.nid = nid;
        this.nnote = nnote;
        this.uid = uid;
        this.unoms = unoms;
        this.uavatar = uavatar;
        this.ndate = ndate;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNnote() {
        return nnote;
    }

    public void setNnote(String nnote) {
        this.nnote = nnote;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUnoms() {
        return unoms;
    }

    public void setUnoms(String unoms) {
        this.unoms = unoms;
    }

    public String getUavatar() {
        return uavatar;
    }

    public void setUavatar(String uavatar) {
        this.uavatar = uavatar;
    }

    public String getNdate() {
        return ndate;
    }

    public void setNdate(String ndate) {
        this.ndate = ndate;
    }
}

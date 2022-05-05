package com.deone.abomo.models;

public class User implements Comparable<User>{
    private String uid;
    private String unoms;
    private String ucover;
    private String uavatar;
    private String utelephone;
    private String uemail;
    private String ucni;
    private String udelivrance;
    private String ucodepostal;
    private String uville;
    private String uadresse;
    private String upays;
    private String udate;

    public User() {
    }

    public User(String uid, String unoms,
                String ucover, String uavatar,
                String utelephone, String uemail,
                String ucni, String udelivrance,
                String ucodepostal, String uville,
                String uadresse, String upays, String udate) {
        this.uid = uid;
        this.unoms = unoms;
        this.ucover = ucover;
        this.uavatar = uavatar;
        this.utelephone = utelephone;
        this.uemail = uemail;
        this.ucni = ucni;
        this.udelivrance = udelivrance;
        this.ucodepostal = ucodepostal;
        this.uville = uville;
        this.uadresse = uadresse;
        this.upays = upays;
        this.udate = udate;
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

    public String getUcover() {
        return ucover;
    }

    public void setUcover(String ucover) {
        this.ucover = ucover;
    }

    public String getUavatar() {
        return uavatar;
    }

    public void setUavatar(String uavatar) {
        this.uavatar = uavatar;
    }

    public String getUtelephone() {
        return utelephone;
    }

    public void setUtelephone(String utelephone) {
        this.utelephone = utelephone;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUcni() {
        return ucni;
    }

    public void setUcni(String ucni) {
        this.ucni = ucni;
    }

    public String getUdelivrance() {
        return udelivrance;
    }

    public void setUdelivrance(String udelivrance) {
        this.udelivrance = udelivrance;
    }

    public String getUcodepostal() {
        return ucodepostal;
    }

    public void setUcodepostal(String ucodepostal) {
        this.ucodepostal = ucodepostal;
    }

    public String getUville() {
        return uville;
    }

    public void setUville(String uville) {
        this.uville = uville;
    }

    public String getUadresse() {
        return uadresse;
    }

    public void setUadresse(String uadresse) {
        this.uadresse = uadresse;
    }

    public String getUpays() {
        return upays;
    }

    public void setUpays(String upays) {
        this.upays = upays;
    }

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }

    @Override
    public int compareTo(User o) {
        return 0;
    }
}

package com.deone.abomo.models;

public class Locataire implements Comparable<Locataire>{

    private String locid;
    private String locnom;
    private String locavatar;
    private String loccover;
    private String locdatebail;
    private String locloyer;
    private String loccni;
    private String locdelivrance;
    private String locdescription;
    private String locdate;

    public Locataire() {
    }

    public Locataire(String locid, String locnom,
                     String locavatar, String loccover,
                     String locdatebail, String locloyer,
                     String loccni, String locdelivrance,
                     String locdescription, String locdate) {
        this.locid = locid;
        this.locnom = locnom;
        this.locavatar = locavatar;
        this.loccover = loccover;
        this.locdatebail = locdatebail;
        this.locloyer = locloyer;
        this.loccni = loccni;
        this.locdelivrance = locdelivrance;
        this.locdescription = locdescription;
        this.locdate = locdate;
    }

    public String getLocid() {
        return locid;
    }

    public void setLocid(String locid) {
        this.locid = locid;
    }

    public String getLocnom() {
        return locnom;
    }

    public void setLocnom(String locnom) {
        this.locnom = locnom;
    }

    public String getLocavatar() {
        return locavatar;
    }

    public void setLocavatar(String locavatar) {
        this.locavatar = locavatar;
    }

    public String getLoccover() {
        return loccover;
    }

    public void setLoccover(String loccover) {
        this.loccover = loccover;
    }

    public String getLocdatebail() {
        return locdatebail;
    }

    public void setLocdatebail(String locdatebail) {
        this.locdatebail = locdatebail;
    }

    public String getLocloyer() {
        return locloyer;
    }

    public void setLocloyer(String locloyer) {
        this.locloyer = locloyer;
    }

    public String getLoccni() {
        return loccni;
    }

    public void setLoccni(String loccni) {
        this.loccni = loccni;
    }

    public String getLocdelivrance() {
        return locdelivrance;
    }

    public void setLocdelivrance(String locdelivrance) {
        this.locdelivrance = locdelivrance;
    }

    public String getLocdescription() {
        return locdescription;
    }

    public void setLocdescription(String locdescription) {
        this.locdescription = locdescription;
    }

    public String getLocdate() {
        return locdate;
    }

    public void setLocdate(String locdate) {
        this.locdate = locdate;
    }

    @Override
    public int compareTo(Locataire o) {
        return 0;
    }
}

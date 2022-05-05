package com.deone.abomo.models;

public class Loyer implements Comparable<Loyer>{
    private String loyid;
    private String loymontant;
    private String loymotif;
    private String loyavance;
    private String loyreste;
    private String locid;
    private String locnom;
    private String locavatar;
    private String loysignature;
    private String loydate;

    public Loyer() {
    }

    public Loyer(String loyid, String loymontant,
                 String loymotif, String loyavance,
                 String loyreste, String locid,
                 String locnom, String locavatar,
                 String loysignature, String loydate) {
        this.loyid = loyid;
        this.loymontant = loymontant;
        this.loymotif = loymotif;
        this.loyavance = loyavance;
        this.loyreste = loyreste;
        this.locid = locid;
        this.locnom = locnom;
        this.locavatar = locavatar;
        this.loysignature = loysignature;
        this.loydate = loydate;
    }

    public String getLoyid() {
        return loyid;
    }

    public void setLoyid(String loyid) {
        this.loyid = loyid;
    }

    public String getLoymontant() {
        return loymontant;
    }

    public void setLoymontant(String loymontant) {
        this.loymontant = loymontant;
    }

    public String getLoymotif() {
        return loymotif;
    }

    public void setLoymotif(String loymotif) {
        this.loymotif = loymotif;
    }

    public String getLoyavance() {
        return loyavance;
    }

    public void setLoyavance(String loyavance) {
        this.loyavance = loyavance;
    }

    public String getLoyreste() {
        return loyreste;
    }

    public void setLoyreste(String loyreste) {
        this.loyreste = loyreste;
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

    public String getLoysignature() {
        return loysignature;
    }

    public void setLoysignature(String loysignature) {
        this.loysignature = loysignature;
    }

    public String getLoydate() {
        return loydate;
    }

    public void setLoydate(String loydate) {
        this.loydate = loydate;
    }

    @Override
    public int compareTo(Loyer o) {
        return 0;
    }
}

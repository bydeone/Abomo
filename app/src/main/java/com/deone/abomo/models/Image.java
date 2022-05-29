package com.deone.abomo.models;

public class Image {
    private String iid;
    private String icover;
    private String ititre;
    private String idescription;
    private String idate;

    public Image() {
    }

    public Image(String iid, String icover, String ititre, String idescription, String idate) {
        this.iid = iid;
        this.icover = icover;
        this.ititre = ititre;
        this.idescription = idescription;
        this.idate = idate;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getIcover() {
        return icover;
    }

    public void setIcover(String icover) {
        this.icover = icover;
    }

    public String getItitre() {
        return ititre;
    }

    public void setItitre(String ititre) {
        this.ititre = ititre;
    }

    public String getIdescription() {
        return idescription;
    }

    public void setIdescription(String idescription) {
        this.idescription = idescription;
    }

    public String getIdate() {
        return idate;
    }

    public void setIdate(String idate) {
        this.idate = idate;
    }
}

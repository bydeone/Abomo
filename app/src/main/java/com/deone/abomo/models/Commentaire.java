package com.deone.abomo.models;

public class Commentaire {
    private String cid;
    private String cmessage;
    private String cnvues;
    private String cnlikes;
    private String cncommentaires;
    private String cnpartage;
    private String cnsignalement;
    private String cdate;
    private String uid;
    private String unoms;
    private String uavatar;

    public Commentaire() {
    }

    public Commentaire(String cid, String cmessage, String cnvues, String cnlikes, String cncommentaires, String cnpartage, String cnsignalement, String cdate, String uid, String unoms, String uavatar) {
        this.cid = cid;
        this.cmessage = cmessage;
        this.cnvues = cnvues;
        this.cnlikes = cnlikes;
        this.cncommentaires = cncommentaires;
        this.cnpartage = cnpartage;
        this.cnsignalement = cnsignalement;
        this.cdate = cdate;
        this.uid = uid;
        this.unoms = unoms;
        this.uavatar = uavatar;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCmessage() {
        return cmessage;
    }

    public void setCmessage(String cmessage) {
        this.cmessage = cmessage;
    }

    public String getCnvues() {
        return cnvues;
    }

    public void setCnvues(String cnvues) {
        this.cnvues = cnvues;
    }

    public String getCnlikes() {
        return cnlikes;
    }

    public void setCnlikes(String cnlikes) {
        this.cnlikes = cnlikes;
    }

    public String getCncommentaires() {
        return cncommentaires;
    }

    public void setCncommentaires(String cncommentaires) {
        this.cncommentaires = cncommentaires;
    }

    public String getCnpartage() {
        return cnpartage;
    }

    public void setCnpartage(String cnpartage) {
        this.cnpartage = cnpartage;
    }

    public String getCnsignalement() {
        return cnsignalement;
    }

    public void setCnsignalement(String cnsignalement) {
        this.cnsignalement = cnsignalement;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
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
}

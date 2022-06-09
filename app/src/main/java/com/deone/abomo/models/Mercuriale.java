package com.deone.abomo.models;

public class Mercuriale {
    private String mid;
    private String mcommune;
    private String mquartier;
    private String mlieudit;
    private String mtypeimmeuble;
    private String mclassification;
    private String mvaleuraumc;

    public Mercuriale() {
    }

    public Mercuriale(String mid, String mcommune, String mquartier, String mlieudit, String mtypeimmeuble, String mclassification, String mvaleuraumc) {
        this.mid = mid;
        this.mcommune = mcommune;
        this.mquartier = mquartier;
        this.mlieudit = mlieudit;
        this.mtypeimmeuble = mtypeimmeuble;
        this.mclassification = mclassification;
        this.mvaleuraumc = mvaleuraumc;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMcommune() {
        return mcommune;
    }

    public void setMcommune(String mcommune) {
        this.mcommune = mcommune;
    }

    public String getMquartier() {
        return mquartier;
    }

    public void setMquartier(String mquartier) {
        this.mquartier = mquartier;
    }

    public String getMlieudit() {
        return mlieudit;
    }

    public void setMlieudit(String mlieudit) {
        this.mlieudit = mlieudit;
    }

    public String getMtypeimmeuble() {
        return mtypeimmeuble;
    }

    public void setMtypeimmeuble(String mtypeimmeuble) {
        this.mtypeimmeuble = mtypeimmeuble;
    }

    public String getMclassification() {
        return mclassification;
    }

    public void setMclassification(String mclassification) {
        this.mclassification = mclassification;
    }

    public String getMvaleuraumc() {
        return mvaleuraumc;
    }

    public void setMvaleuraumc(String mvaleuraumc) {
        this.mvaleuraumc = mvaleuraumc;
    }
}

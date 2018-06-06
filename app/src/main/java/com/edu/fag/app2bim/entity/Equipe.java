package com.edu.fag.app2bim.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Equipe {
    @SerializedName("cdEquipe")
    @Expose
    private String cdEquipe;
    @SerializedName("brEquipe")
    @Expose
    private String brEquipe;
    @SerializedName("corEquipe")
    @Expose
    private String corEquipe;
    @SerializedName("nmEquipe")
    @Expose
    private String nmEquipe;
    @SerializedName("nmComum")
    @Expose
    private String nmComum;
    @SerializedName("nmSlug")
    @Expose
    private String nmSlug;
    @SerializedName("sgEquipe")
    @Expose
    private String sgEquipe;
    @SerializedName("tgEquipe")
    @Expose
    private String tgEquipe;
    @SerializedName("tpEquipe")
    @Expose
    private String tpEquipe;
    @SerializedName("uriEquipe")
    @Expose
    private String uriEquipe;

    public String getCdEquipe() {
        return cdEquipe;
    }

    public void setCdEquipe(String cdEquipe) {
        this.cdEquipe = cdEquipe;
    }

    public String getBrEquipe() {
        return brEquipe;
    }

    public void setBrEquipe(String brEquipe) {
        this.brEquipe = brEquipe;
    }

    public String getCorEquipe() {
        return corEquipe;
    }

    public void setCorEquipe(String corEquipe) {
        this.corEquipe = corEquipe;
    }

    public String getNmEquipe() {
        return nmEquipe;
    }

    public void setNmEquipe(String nmEquipe) {
        this.nmEquipe = nmEquipe;
    }

    public String getNmComum() {
        return nmComum;
    }

    public void setNmComum(String nmComum) {
        this.nmComum = nmComum;
    }

    public String getNmSlug() {
        return nmSlug;
    }

    public void setNmSlug(String nmSlug) {
        this.nmSlug = nmSlug;
    }

    public String getSgEquipe() {
        return sgEquipe;
    }

    public void setSgEquipe(String sgEquipe) {
        this.sgEquipe = sgEquipe;
    }

    public String getTgEquipe() {
        return tgEquipe;
    }

    public void setTgEquipe(String tgEquipe) {
        this.tgEquipe = tgEquipe;
    }

    public String getTpEquipe() {
        return tpEquipe;
    }

    public void setTpEquipe(String tpEquipe) {
        this.tpEquipe = tpEquipe;
    }

    public String getUriEquipe() {
        return uriEquipe;
    }

    public void setUriEquipe(String uriEquipe) {
        this.uriEquipe = uriEquipe;
    }

    @Override
    public String toString() {
        return cdEquipe.toString() + nmEquipe;
    }
}
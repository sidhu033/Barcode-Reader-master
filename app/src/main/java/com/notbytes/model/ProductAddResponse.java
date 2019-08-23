package com.notbytes.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductAddResponse
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("isSucess")
    @Expose
    private Boolean isSucess;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Getcountries")
    @Expose
    private Object getcountries;
    @SerializedName("StatesList")
    @Expose
    private Object statesList;
    @SerializedName("CitiesList")
    @Expose
    private Object citiesList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(Boolean isSucess) {
        this.isSucess = isSucess;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getGetcountries() {
        return getcountries;
    }

    public void setGetcountries(Object getcountries) {
        this.getcountries = getcountries;
    }

    public Object getStatesList() {
        return statesList;
    }

    public void setStatesList(Object statesList) {
        this.statesList = statesList;
    }

    public Object getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(Object citiesList) {
        this.citiesList = citiesList;
    }

    @Override
    public String toString() {
        return "ProductAddResponse{" +
                "message='" + message + '\'' +
                ", statusCode=" + statusCode +
                ", isSucess=" + isSucess +
                ", id=" + id +
                ", getcountries=" + getcountries +
                ", statesList=" + statesList +
                ", citiesList=" + citiesList +
                '}';
    }
}


package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Fines{

    @SerializedName("fine_amount") private String fineAmount;
    @SerializedName("fine_id") private String fineId;
    @SerializedName("fine_status") private String fineStatus;
    @SerializedName("fine_reason") private String fineReason;
    @SerializedName("date_fined") private String dateFined;


    public Fines(String fineAmount, String fineId, String fineStatus,String fineReason,String dateFined) {

        this.fineAmount=fineAmount;
        this.fineStatus=fineStatus;
        this.dateFined=dateFined;
        this.fineReason=fineReason;
        this.fineId=fineId;
    }


    public String getFineId() {
        return fineId;
    }

    public String getFineAmount(){return fineAmount;}

    public String getFineStatus() {
        return fineStatus;
    }

    public String getFineReason() {
        return fineReason;
    }

    public String getDateFined() {
        return dateFined;
    }



    public String setfineId() {
        return fineId;
    }



}

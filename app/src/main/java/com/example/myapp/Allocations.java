package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Allocations{

    @SerializedName("member_id") private String memberId;
    @SerializedName("allocation_id") private String allocationId;
    @SerializedName("chama_id") private String chamaId;
    @SerializedName("allocation_amount") private String allocationAmount;
    @SerializedName("allocation_date") private String date;


    public Allocations(String memberId, String allocationId, String allocationAmount,String date,String chamaId) {

        this.chamaId=chamaId;
        this.allocationAmount=allocationAmount;
        this.allocationId=allocationId;
        this.memberId=memberId;
        this.date=date;
    }


    public String getMemberId() {
        return memberId;
    }

    public String getChamaId(){return chamaId;}

    public String getAllocationId() {
        return allocationId;
    }

    public String getAllocationAmount() {
        return allocationAmount;
    }

    public String getDate() {
        return date;
    }






}

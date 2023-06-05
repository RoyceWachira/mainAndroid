package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Withdrawals{

    @SerializedName("withdrawal_amount") private String withdrawalAmount;
    @SerializedName("withdrawal_id") private String withdrawalId;
    @SerializedName("withdrawal_reason") private String withdrawalReason;


    public Withdrawals(String withdrawalAmount, String withdrawalId, String withdrawalReason) {

        this.withdrawalAmount=withdrawalAmount;
        this.withdrawalId=withdrawalId;
        this.withdrawalReason=withdrawalReason;

    }


    public String getWithdrawalId() {
        return withdrawalId;
    }

    public String getWithdrawalAmount(){return withdrawalAmount;}

    public String getWithdrawalReason() {
        return withdrawalReason;
    }



    public String setwithdrawalId() {
        return withdrawalId;
    }



}

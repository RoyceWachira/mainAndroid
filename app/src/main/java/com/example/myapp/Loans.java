package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Loans{

    @SerializedName("loan_amount") private String loanAmount;
    @SerializedName("loan_id") private String loanId;
    @SerializedName("loan_status") private String loanStatus;
    @SerializedName("amount_payable") private String amountPayable;
    @SerializedName("due_at") private String dueAt;
    @SerializedName("chama_id") private String chamaId;


    public Loans(String loanAmount, String loanId, String loanStatus,String amountPayable,String dueAt, String chamaId) {

        this.loanAmount=loanAmount;
        this.loanStatus=loanStatus;
        this.dueAt=dueAt;
        this.amountPayable=amountPayable;
        this.loanId=loanId;
        this.chamaId=chamaId;
    }

    public String getChamaId() {return chamaId; }

    public String getLoanId() {
        return loanId;
    }

    public String getLoanAmount(){return loanAmount;}

    public String getLoanStatus() {
        return loanStatus;
    }

    public String getAmountPayable() {
        return amountPayable;
    }

    public String getDueAt() {
        return dueAt;
    }



    public String setLoanId() {
        return loanId;
    }



}

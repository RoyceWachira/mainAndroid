package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Investments{

    @SerializedName("investment_amount") private String investmentAmount;
    @SerializedName("investment_id") private String investmentId;
    @SerializedName("investment_date") private String investmentDate;
    @SerializedName("investment_area") private String investmentArea;
    @SerializedName("expected returns") private String expectedReturns;
    @SerializedName("investment_duration") private String investmentDuration;


    public Investments(String investmentId, String investmentAmount, String investmentArea,String investmentDate,String investmentDuration, String expectedReturns) {

        this.investmentAmount=investmentAmount;
        this.investmentArea=investmentArea;
        this.investmentId=investmentId;
        this.investmentDate=investmentDate;
        this.investmentDuration=investmentDuration;
        this.expectedReturns=expectedReturns;
    }

    public String getInvestmentId() {return investmentId; }

    public String getInvestmentAmount() {
        return investmentAmount;
    }

    public String getInvestmentDate(){return investmentDate;}

    public String getInvestmentArea() {
        return investmentArea;
    }

    public String getExpectedReturns() {
        return expectedReturns;
    }

    public String getInvestmentDuration() {
        return investmentDuration;
    }






}

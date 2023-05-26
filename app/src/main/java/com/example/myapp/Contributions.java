package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Contributions{

    @SerializedName("contribution_amount") private String contributionAmount;
    @SerializedName("contribution_id") private String contributionId;
    @SerializedName("contribution_date") private String contributionDate;


    public Contributions(String contributionAmount, String contributionId, String contributionDate) {

        this.contributionAmount=contributionAmount;
        this.contributionId=contributionId;
        this.contributionDate=contributionDate;

    }


    public String getContributionId() {
        return contributionId;
    }

    public String getContributionAmount(){return contributionAmount;}

    public String getContributionDate() {
        return contributionDate;
    }



    public String setContributionId() {
        return contributionId;
    }



}

package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Chamas {

    @SerializedName("chama_name") private String chamaName;
    @SerializedName("chama_description") private String chamaDescription;
    @SerializedName("chama_id") private String chamaId;
    @SerializedName("system_flow") private String systemFlow;
    @SerializedName("contribution_target") private String contributionTarget;
    @SerializedName("contribution_period") private String contributionPeriod;

    private boolean requestMade;

    public Chamas(String chamaName, String chamaDescription, String chamaId,String systemFlow,String contributionPeriod,String contributionTarget) {
        this.chamaName= chamaName;
        this.chamaDescription=chamaDescription;
        this.chamaId=chamaId;
        this.systemFlow= systemFlow;
        this.contributionPeriod= contributionPeriod;
        this.contributionTarget= contributionTarget;
    }

    public String getSystemFlow() {return  systemFlow;}

    public String getChamaName() {
        return chamaName;
    }

    public String getChamaId() {
        return chamaId;
    }

    public String getChamaDescription() {
        return chamaDescription;
    }


    public void setChamaName(String chamaName) {
        this.chamaName=chamaName;

    }

    public String setChamaId() {
        return chamaId;
    }

    public void setChamaDescription(String chamaDescription) {
        this.chamaDescription=chamaDescription;
    }

    public boolean isRequestMade() {
        return requestMade;
    }

    public void setRequestMade(boolean requestMade) {
        this.requestMade = requestMade;
    }
}

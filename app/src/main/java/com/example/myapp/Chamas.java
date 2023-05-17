package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Chamas {

    @SerializedName("chama_name") private String chamaName;
    @SerializedName("chama_description") private String chamaDescription;
    @SerializedName("chama_id") private Integer chamaId;

    private boolean requestMade;

    public Chamas( String chamaName, String chamaDescription,Integer chamaId) {
        this.chamaName= chamaName;
        this.chamaDescription=chamaDescription;
        this.chamaId=chamaId;
    }



    public String getChamaName() {
        return chamaName;
    }

    public Integer getChamaId() {
        return chamaId;
    }

    public String getChamaDescription() {
        return chamaDescription;
    }


    public void setChamaName(String chamaName) {
        this.chamaName=chamaName;

    }

    public Integer setChamaId() {
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

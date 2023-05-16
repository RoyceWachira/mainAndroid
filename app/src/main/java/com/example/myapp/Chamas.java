package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Chamas {

    @SerializedName("chama_name") private String chamaName;
    @SerializedName("chama_description") private String chamaDescription;

    public Chamas( String chamaName, String chamaDescription) {
        this.chamaName= chamaName;
        this.chamaDescription=chamaDescription;
    }



    public String getChamaName() {
        return chamaName;
    }

    public String getChamaDescription() {
        return chamaDescription;
    }



    public void setChamaName(String chamaName) {
        this.chamaName=chamaName;

    }

    public void setChamaDescription(String chamaDescription) {
        this.chamaDescription=chamaDescription;
    }
}

package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Chamas {
    @SerializedName("chamaId") private int chamaId;
    @SerializedName("chamaName") private int chamaName;
    @SerializedName("chamaDescripion") private int chamaDescripion;

    public Chamas(int chamaId, String chamaName, String chamaDescription) {
    }

    public int getChamaId() {
        return chamaId;
    }

    public int getChamaName() {
        return chamaName;
    }

    public int getChamaDescripion() {
        return chamaDescripion;
    }

    public void setChamaId(int chamaId) {
    }

    public void setChamaName(String chamaName) {
    }

    public void setChamaDescription(String chamaDescription) {
    }
}

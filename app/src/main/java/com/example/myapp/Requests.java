package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Requests{
    @SerializedName("chama_id") private String chamaId;
    @SerializedName("request_id") private String requestId;
    @SerializedName("join_status") private String joinStatus;
    @SerializedName("requested_at") private String requestedAt;



    public Requests(String requestId, String joinStatus, String requestedAt,String chamaId) {
        this.chamaId=chamaId;
        this.requestId=requestId;
        this.requestedAt=requestedAt;
        this.joinStatus=joinStatus;
    }

    public String getChamaId(){return chamaId;}

    public String getRequestId() {return requestId; }

    public String getJoinStatus() {
        return joinStatus;
    }

    public String getRequestedAt(){return requestedAt;}




}

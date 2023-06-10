package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Members {

    @SerializedName("chama_role") private String chamaRole;
    @SerializedName("user_id") private String userId;
    @SerializedName("chama_id") private String chamaId;
    @SerializedName("member_id") private String memberId;
    @SerializedName("username") private String userName;
    @SerializedName("first_name") private String firstName;
    @SerializedName("last_name") private String lastName;
    @SerializedName("email") private String email;
    @SerializedName("date_joined") private String dateJoined;
    @SerializedName("phone_number") private String phoneNumber;


    public Members(String chamaRole, String userId, String firstName,String lastName,String email,String dateJoined, String phoneNumber,String userName, String chamaId, String memberId) {

        this.memberId= memberId;
        this.chamaId=chamaId;
        this.chamaRole=chamaRole;
        this.userId=userId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.dateJoined=dateJoined;
        this.phoneNumber=phoneNumber;
        this.userName=userName;
    }

    public String getMemberId(){return memberId;}

    public String getChamaId(){return chamaId;}

    public String getUserId() {
        return userId;
    }

    public String getUserName(){return userName;}

    public String getChamaRole() {
        return chamaRole;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setChamaRole(String chamaRole) {
        this.chamaRole=chamaRole;
    }

}

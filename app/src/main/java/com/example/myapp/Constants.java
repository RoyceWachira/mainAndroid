package com.example.myapp;

public class Constants {

    private static final String ROOT_URL="http://192.168.100.5/Api/";

    public static final String URL_REGISTER= ROOT_URL+"register.php";
    public static final String URL_LOGIN= ROOT_URL+"login.php";
    public static final String URL_FORGOT_PASSWORD= ROOT_URL+"sendMail.php";
    public static final String URL_ALL_CHAMAS= ROOT_URL+"getallchamas.php";
    public static final String URL_TOTAL_CHAMAS= ROOT_URL+"getChamasTotal.php";
    public static final String URL_TOTAL_USERS= ROOT_URL+"getUsersTotal.php";
    public static final String URL_USER_JOINED_CHAMAS= ROOT_URL+"getUserJoinedChamas.php";
    public static final String URL_GET_USER= ROOT_URL+"getOneUser.php";
    public static final String URL_UPDATE_USER= ROOT_URL+"updateUser.php";
    public static final String URL_CHAMAS_NOT_JOINED= ROOT_URL+"chamasNotJoined.php";
    public static final String URL_NEW_CHAMA= ROOT_URL+"chama.php";
    public static final String URL_REQUEST_JOIN_CHAMA= ROOT_URL+ "joinchamarequest.php";
    public static final String URL_TOTAL_CHAMA_FUNDS= ROOT_URL+"getChamaTotalFunds.php";
    public static final String URL_TOTAL_INDIVIDUAL_LOANS= ROOT_URL+"gettotalMemberLoans.php";
    public static final String URL_TOTAL_CHAMA_MEMBERS= ROOT_URL+"getMembersCount.php";
    public static final String URL_TOTAL_INDIVIDUAL_CONTRIBUTIONS= ROOT_URL+"getTotalIndividualContributions.php";
    public static final String URL_TOTAL_INDIVIDUAL_FINES= ROOT_URL+"getTotalMemberFines.php";
    public static final String URL_GET_MEMBERS= ROOT_URL+"getChamaMembers.php";
}

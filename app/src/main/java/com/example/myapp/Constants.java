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
    public static final String URL_GET_MEMBER_FINES= ROOT_URL+"getMemberFines.php";
    public static final String URL_GET_MEMBER_LOANS= ROOT_URL+"getMemberLoans.php";
    public static final String URL_GET_MEMBER_CONTRIBUTIONS= ROOT_URL+"getMemberContributions.php";
    public static final String URL_IS_LEADER= ROOT_URL+"hasLeadershipRole.php";
    public static final String URL_ALL_CONRIBUTIONS= ROOT_URL+"getAllContributions.php";
    public static final String URL_MAKE_CONRIBUTION= ROOT_URL+"makeContribution.php";
    public static final String URL_CHECK_SYSTEM_FLOW= ROOT_URL+"checkSystemFlow.php";
    public static final String URL_MAKE_WITHDRAWAL= ROOT_URL+"makeWithdrawal.php";
    public static final String URL_IS_TREASURER= ROOT_URL+"isTreasurer.php";
    public static final String URL_ALL_WITHDRAWALS= ROOT_URL+"getAllWithdrawals.php";
    public static final String URL_ALL_FINES= ROOT_URL+"getAllFines.php";
    public static final String URL_CHARGE_FINE= ROOT_URL+"chargeFine.php";
    public static final String URL_ALL_LOANS= ROOT_URL+"getAllLoans.php";
    public static final String URL_REQUEST_LOAN= ROOT_URL+"loanrequest.php";
    public static final String URL_GET_REQUESTED_LOANS= ROOT_URL+"getLoanRequests.php";
    public static final String URL_APPROVE_LOAN= ROOT_URL+"approveLoan.php";
    public static final String URL_REJECT_LOAN= ROOT_URL+"rejectLoan.php";
    public static final String URL_ALL_MEETINGS= ROOT_URL+"getAllMeetings.php";
    public static final String URL_CREATE_MEETING= ROOT_URL+"createMeeting.php";
    public static final String URL_JOIN_REQUESTS= ROOT_URL+"getJoinRequests.php";
    public static final String URL_ACCEPT_REQUEST= ROOT_URL+"approveRequest.php";
    public static final String URL_REJECT_REQUEST= ROOT_URL+"rejectRequest.php";
    public static final String URL_GET_CHAMA= ROOT_URL+"getchama.php";
    public static final String URL_IS_CHAIR_OR_VICE= ROOT_URL+"isChairOrVice.php";
    public static final String URL_CHANGE_ROLE= ROOT_URL+"updateRole.php";
    public static final String URL_DEMOTE= ROOT_URL+"demote.php";
    public static final String URL_MAKE_CHAIR= ROOT_URL+"makeChair.php";
    public static final String URL_GET_NOTIFICATIONS= ROOT_URL+"getAllNotifications.php";
    public static final String URL_UPDATE_CHAMA= ROOT_URL+"updateChama.php";
    public static final String URL_GET_NAME= ROOT_URL+"getName.php";
    public static final String URL_LEAVE_CHAMA= ROOT_URL+"leaveChama.php";
}

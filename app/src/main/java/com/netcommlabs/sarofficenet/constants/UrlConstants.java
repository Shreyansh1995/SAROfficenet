package com.netcommlabs.sarofficenet.constants;

/**
 * Created by Flash_Netcomm on 2/16/2019.
 */

public class UrlConstants {
    public static final String TOKEN_KEY = "TokenNo";
    public static final String TOKEN_NO = "abcHkl7900@8Uyhkj";

    public static final String BASE_URL = "http://192.168.0.18:7021/MobileAPI/AppServices.svc/";
//    public static final String BASE_URL = "http://humsar.sar-group.com/MobileAPI/AppServices.svc/";

    public static final String USER_AUTH = BASE_URL + "UsersAuth";
    public static final String FORGOT_PASSWORD = BASE_URL + "ForgetPassword";
    public static final String LOG_OUT = BASE_URL + "LogoutAuth";
    public static final String UPLOAD_IMAGE = BASE_URL + "MyPage_UpdateProfilePic";

    //--------------------Gallery module
    public static final String GALLERY_CATEGORY= BASE_URL + "Gallery_PhotoCategoryList";
    public static final String GALLERY_LIST= BASE_URL + "Gallery_GalleryList";

    //-------------------------Attandence Calender-----------------
    public static final String GET_ATTENDANCE = BASE_URL + "GetMyAttendance";

//-------------------------------New Joinee---------------------

    public static final String NEW_JOINEE = BASE_URL + "HRCorner_NewJoineeList";
    public static final String BIRTHDAY = BASE_URL + "HRCorner_BirthdayList";

    //-------------------------- Holiday List -----------------------
    public static final String HOLIDAY_LIST = BASE_URL + "GetHolidayList";
    public static final String LOCATION_LIST = BASE_URL + "GetLocationList";

    //-------------------Team List -------------------------

    public static final String MY_TEAM_LIST = BASE_URL + "GetMyTeamList";

    //--------------- Attendance Regularization ------------------
    public static final String OD_GET_BASIC_DETAILS = BASE_URL + "OD_GetBasicDetails";
    public static final String SUBMIT_ATTENDANCE_REGULARIZATION_TYPE = BASE_URL + "OD_SubmitRequest";
    public static final String GET_REGULERIZAE_DETAIL = BASE_URL + "OD_MyRequest";
    public static final String VIEW_MY_REGULERIZE_DETAIL = BASE_URL + "OD_ViewRequestDetails";
    public static final String OD_CANCEL_REQUEST = BASE_URL + "OD_CancelledRequest";
    public static final String PENDING_ATTENDANCE_REQUEST = BASE_URL + "OD_PendingRequest";
    public static final String OD_APPROVE_REQUEST = BASE_URL + "OD_ApproveRequest";
    public static final String OD_DISAPPROVE_REQUEST = BASE_URL + "OD_DisapproveRequest";
    public static final String OD_ARCHIVE_REQUEST= BASE_URL + "OD_ArchiveRequest";


    //*******************Sort Leave ***********************
    public static final String SL_BASIC_DETAIL = BASE_URL + "SL_GetBasicDetails";
    public static final String SL_SUBMIT_REQUEST = BASE_URL + "SL_SubmitRequest";
    public static final String SL_DETAIL_LIST = BASE_URL + "SL_MyRequest";
    public static final String SL_PENDING_LIST = BASE_URL + "SL_PendingRequest";
    public static final String SL_ARCHIVE_LIST = BASE_URL + "SL_ArchiveRequest";
    public static final String SL_VIEW_DETAIL= BASE_URL + "SL_ViewRequestDetails";
    public static final String SL_CANCEL_REQUEST= BASE_URL + "SL_CancelledRequest";
    public static final String SL_APPROVE_REQUEST= BASE_URL + "SL_ApproveRequest";
    public static final String SL_DISAPPROVE_REQUEST= BASE_URL + "SL_DisapproveRequest";
    public static final String SL_GET_DATA_ON_CHANGED = BASE_URL + "SL_GetDataOnDateChange";


    /***********************************Leave Module*************************************/

    public static final String CALCULATE_LEAVE = BASE_URL + "Leave_CalculateDays";
    public static final String GET_LEAVE_BALANCE = BASE_URL + "Leave_GetBalance";
    public static final String SUBMIT_LEAVE_REQUEST = BASE_URL + "leaveSubmitRequest";
    public static final String VIEW_MY_LEAVE_REQUEST_IN_POPUP = BASE_URL + "LeaveViewRequestDetails";
    public static final String VIEW_LEAVE_DEATILS = BASE_URL + "LeaveHistoryList";
    public static final String GET_DATA_ON_LEAVE_CHANGE_TYPE = BASE_URL + "Leave_GetDataOnLeaveTypeChange";
    public static final String GET_PENDING_LIST = BASE_URL + "LeavePendingList";
    public static final String LEAVE_APPROVE_DISAPPROVE_REQUEST = BASE_URL + "LeaveApproveDisApprove";
    public static final String LEAVE_ARCHIVE_LIST = BASE_URL + "LeaveArchiveList";
    public static final String LEAVE_CANCEL_REQUEST = BASE_URL + "CancelLeave";
    public static final String LEAVE_USER_CANCEL_DEATILS = BASE_URL + "LeaveUserCancelDetails";
    public static final String LEAVE_VALIDATE_REQUEST= BASE_URL + "Leave_ValidateRequest";

    /***********************************Pending Request Dashboard Module*************************************/

    public static final String GET_PENDING_DATA_DASHBOARD = BASE_URL + "Dashboard_GetPendingData";
    public static final String SEND_TOKEN = BASE_URL + "SaveFCMDeviceID";
    public static final String GET_OFFICES= BASE_URL + "MyPage_GetMyOfficesList";

    /***********************************Notes Dashboard Module*************************************/

    public static final String NOTES= BASE_URL + "MyPage_GetNotesList";
    public static final String DELETE_NOTES= BASE_URL + "MyPage_DailyNotesDelete";
    public static final String ADD_NOTES= BASE_URL + "MyPage_DailyNotesSubmit";


    /***********************************Suggestion Dashboard Module*************************************/

    public static final String SUGGESTIONWITHIDENTITY= BASE_URL + "Suggestion_SendWithIdentity";
    public static final String SUGGESTIONWITHOUTIDENTITY= BASE_URL + "Suggestion_SendByAnonymous";

    /***********************************Notification*************************************/
    public static final String GET_NOTIFICATION_LIST= BASE_URL + "GetPushNotificationList";
    public static final String MARK_NOTIFICATION= BASE_URL + "MarkNotification";
    public static final String DELETE_NOTIFICATION= BASE_URL + "ClearPushNotification";
    public static final String HELP= BASE_URL + "GetContactDetails";

    /***********************************HelpDesk*************************************/
    public static final String GET_DEPARTMENT= BASE_URL + "HelpDesk_DepartmentList";
    public static final String GET_CATEGORYLIST= BASE_URL + "HelpDesk_CategoryList";
    public static final String GET_SUBCATEGORYLIST= BASE_URL + "HelpDesk_SubCategoryList";
    public static final String SUBMITHELPDESK= BASE_URL + "HelpDesk_SubmitRequest";
    public static final String VALIDATEREF= BASE_URL + "HelpDesk_ValidateTicketRefNo";

    public static final String TICKET_LIST= BASE_URL + "HelpDesk_GetDetails";
    public static final String TICKET_LIST_DETAIL= BASE_URL + "HelpDesk_ViewDetails";
    public static final String IS_ADMIN= BASE_URL + "HelpDesk_IsHelpDeskAdmin";

    public static final String TICKET_LIST_ADMIN= BASE_URL + "HelpDesk_AdminRequestList";
    public static final String CATEGORY_LIST_ADMIN= BASE_URL + "HelpDesk_AdminCategoryList";
    public static final String SUBMITTEDBY= BASE_URL + "HelpDesk_SubmittedByUsersList";


    public static final String HOLD= BASE_URL + "HelpDesk_Hold";
    public static final String SOLVED= BASE_URL + "HelpDesk_Solved";
    public static final String REASSIGN= BASE_URL + "HelpDesk_ReassignSubmitRequest";

    public static final String SUBMIT_STATUS= BASE_URL + "HelpDesk_SubmitStatus";
    public static final String ADMIN_VIEW_DETAILS= BASE_URL + "HelpDesk_AdminViewDetails";

    /***********************************Punch In/out*************************************/
    public static final String GET_STATUS= BASE_URL + "GetInOutStatus";
    public static final String SAVEPUNCHINOOUT= BASE_URL + "SavePunchInOutDetails";
    //------------------Tags--------------------
    public static final int USER_AUTH_TAG = 1001;
    public static final int GALLERY_CATEGORY_TAG = 1002;
    public static final int GALLERY_LIST_TAG = 1003;
    public static final int GET_ATTENDANCE_TAG = 1004;
    public static final int NEW_JOINEE_TAG = 1005;
    public static final int BIRTHDAY_TAG = 1006;
    public static final int LOG_OUT_TAG = 1007;
    public static final int HOLIDAY_LIST_TAG = 1008;
    public static final int UPLOAD_IMAGE_TAG = 1009;
    public static final int MY_TEAM_LIST_TAG = 1010;
    public static final int OD_GET_BASIC_DETAILS_TAG = 1011;
    public static final int SUBMIT_ATTENDANCE_REGULARIZATION_TYPE_TAG = 1012;
    public static final int GET_REGULERIZAE_DETAIL_TAG = 1013;
    public static final int VIEW_MY_REGULERIZE_DETAIL_TAG = 1014;
    public static final int OD_CANCEL_REQUEST_TAG = 1015;
    public static final int PENDING_ATTENDANCE_REQUEST_TAG = 1016;
    public static final int OD_APPROVE_REQUEST_TAG = 1017;
    public static final int OD_DISAPPROVE_REQUEST_TAG = 1018;;
    public static final int OD_ARCHIVE_REQUEST_TAG = 1019;;
    public static final int SL_BASIC_DETAIL_TAG = 1020;;
    public static final int SL_GET_DATA_ON_CHANGED_TAG = 1021;;
    public static final int SL_SUBMIT_REQUEST_TAG = 1022;;
    public static final int SL_DETAIL_LIST_TAG = 1023;
    public static final int SL_VIEW_DETAIL_TAG = 1024;
    public static final int SL_PENDING_LIST_TAG = 1025;
    public static final int SL_ARCHIVE_LIST_TAG = 1026;
    public static final int SL_CANCEL_REQUEST_TAG = 1027;
    public static final int SL_APPROVE_REQUEST_TAG = 1028;
    public static final int SL_DISAPPROVE_REQUEST_TAG = 1029;


    //-----------------leave Tags-------------------
    public static final int GET_DATA_ON_LEAVE_CHANGE_TYPE_TAG = 1030;
    public static final int GET_LEAVE_BALANCE_TAG = 1031;
    public static final int CALCULATE_LEAVE_TAG = 1032;
    public static final int LEAVE_VALIDATE_REQUEST_TAG = 1033;
    public static final int SUBMIT_LEAVE_REQUEST_TAG = 1034;
    public static final int VIEW_LEAVE_DEATILS_TAG = 1035;
    public static final int VIEW_MY_LEAVE_REQUEST_IN_POPUP_TAG = 1036;
    public static final int LEAVE_APPROVE_DISAPPROVE_REQUEST_TAG = 1037;
    public static final int LEAVE_CANCEL_REQUEST_TAG = 1038;
    public static final int GET_PENDING_LIST_TAG = 1039;
    public static final int LEAVE_ARCHIVE_LIST_TAG = 1040;
    public static final int LEAVE_USER_CANCEL_DEATILS_TAG = 1041;
    public static final int LOCATION_LIST_TAG = 1042;
    public static final int GET_PENDING_DATA_DASHBOARD_TAG = 1044;
    public static final int GET_OFFICES_TAG = 1045;
    public static final int NOTES_TAG = 1046;
    public static final int DELETE_NOTES_TAG = 1047;
    public static final int ADD_NOTES_TAG = 1048;
    public static final int SUGGESTIONWITHIDENTITY_TAG = 1049;
   // public static final int SUGGESTIONWITHOUTIDENTITY_TAG = 1050;
    public static final int SEND_TOKEN_TAG = 1050;
    public static final int GET_NOTIFICATION_LIST_TAG = 1051;
    public static final int MARK_NOTIFICATION_TAG = 1052;
    public static final int DELETE_NOTIFICATION_TAG = 1053;
    public static final int FORGOT_PASSWORD_TAG = 1054;
    public static final int HELP_TAG = 1055;
    public static final int GET_DEPARTMENT_TAG = 1056;
    public static final int GET_CATEGORYLIST_TAG = 1057;
    public static final int GET_SUBCATEGORYLIST_TAG = 1058;
    public static final int SUBMITHELPDESK_TAG = 1059;
    public static final int VALIDATEREF_TAG = 1060;
    public static final int TICKET_LIST_TAG = 1061;
    public static final int TICKET_LIST_DETAIL_TAG = 1062;
    public static final int IS_ADMIN_TAG = 1063;
    public static final int TICKET_LIST_ADMIN_TAG = 1064;
    public static final int CATEGORY_LIST_ADMIN_TAG = 1065;
    public static final int SUBMITTEDBY_TAG = 1066;
    public static final int HOLD_TAG = 1067;
    public static final int SOLVED_TAG = 1068;
    public static final int REASSIGNED_TAG = 1069;
    public static final int GET_STATUS_TAG = 1070;
    public static final int SAVEPUNCHINOOUT_TAG = 1071;
    public static final int SUBMIT_STATUS_TAG = 1072;
    public static final int ADMIN_VIEW_DETAILS_TAG = 1073;
}

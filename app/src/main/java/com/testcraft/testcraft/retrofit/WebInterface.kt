package com.testcraft.testcraft.retrofit

import com.google.gson.JsonObject
import com.testcraft.testcraft.models.*
import com.testcraft.testcraft.sectionmodule.NewQuestionResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface WebInterface {

    @FormUrlEncoded
    @POST("Get_Activity_Action")
    fun getToken(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Add_Student_Mobile")
    fun getSignup(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_Student_Login")
    fun getLogin(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("SP_Check_Student_Duplicate_Email")
    fun checkEmail(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    //    http://webservice.testcraft.in/WebService.asmx/SP_Check_Student_Duplicate_Mobile
    @FormUrlEncoded
    @POST("SP_Check_Student_Duplicate_Mobile")
    fun checkMobile(@Field("Mobile") mobile: String): Call<VerifyMobileData>

    @FormUrlEncoded
    @POST("SP_Check_Student_Duplicate_Mobile")
    fun checkMobile2(@Field("Mobile") mobile: String): Observable<VerifyMobileData>

    @FormUrlEncoded
    @POST("Update_Student")
    fun updateProfile(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Forgot_Password")
    fun forgotPassword(@FieldMap map: HashMap<String, String>): Observable<JsonObject>

    @FormUrlEncoded
    @POST("Change_Password")
    fun changePassword(@FieldMap map: HashMap<String, String>): Call<JsonObject>

//    @FormUrlEncoded
//    @POST("Add_Student_Preference")
//    fun AddPrefrence(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @GET("Get_CourseType_List")
    fun getCourseList(): Observable<PackageData>

    @FormUrlEncoded
    @POST("Get_Course_List")
    fun getCourseTypeList(@Field("CourseTypeID") coursetype: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_CourseSubject_List")
    fun getCourseSubjectList(@Field("CourseID") courseid: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_BoardStandard_List")
    fun getBoardStandardList(@Field("BoardID") boardid: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_BoardStandardSubject_List")
    fun getBoardStandardSubjectList(@Field("BoardID") boardid: String, @Field("StandardID") standardid: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Add_Seach_History")
    fun addSearchHistory(@Field("StudentID") stuid: String, @Field("SearchText") searchtxt: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_Seach_History")
    fun getSearchHistory(@Field("StudentID") boardid: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("GeneratePaymentRequest")
    fun getPayment(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Update_Payment_Request")
    fun updatePaymentStatus(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Update_Payment_Request_Free")
    fun updatePaymentStatusNew(@FieldMap map: HashMap<String, String>): Call<TestListModel>

    @FormUrlEncoded
    @POST("Get_TestPackageName_By_ID")
    fun getPackage(@FieldMap map: HashMap<String, String>): Call<GetMarketPlaceData>

    @FormUrlEncoded
    @POST("Get_TestPackage_By_ID_New")
    fun getPackageDetail(@Field("TestPackageID") testPackageID: String, @Field("StudentID") stuid: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_Student_PaymentTransaction_List")
    fun getMyPayment(@Field("StudentID") boardid: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_Student_SubcriptionPaymentTransaction_List")
    fun getMySubscription(@Field("StudentID") boardid: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Add_StudentTestPackage")
    fun addTestPackage(@Field("StudentID") stuid: String, @Field("TestPackageID") pkgid: String): Call<JsonObject>

//    @FormUrlEncoded
//    @POST("Get_StudentTestPackage")
//    fun myTestPackage(@Field("StudentID") stuid: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_StudentTest")
    fun getTestList(@Field("StudentID") stuid: String, @Field("StudentTestPackageID") StudentTestPackageID: String): Call<TestListModel>

//    @FormUrlEncoded
//    @POST("Get_Student_TestQuestion")
//    fun getQuestions(@Field("TestID") testid: String, @Field("StudentTestID") stu_testid: String): Call<QuestionResponse>

    @FormUrlEncoded
    @POST("Get_Student_StudentTestAnswer_New_aws")
    fun getNewQuestions(@Field("TestID") testid: String, @Field("StudentTestID") stu_testid: String): Call<NewQuestionResponse>

    @FormUrlEncoded
    @POST("Get_Tutor_By_TutorID")
    fun getTutorProfile(@Field("TutorID") stuid: String): Call<TutorModel>

    @FormUrlEncoded
    @POST("Get_TutorNameBy_Criteria")
    fun getTutorFilterName(@FieldMap map: HashMap<String, String>): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_TestPackageName_By_TutorID")
    fun getTutorSimilarPkgs(@Field("TutorID") stuid: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_TutorRating")
    fun getTutorRating(@Field("TutorID") stuid: String): Call<TutorModel>

    @FormUrlEncoded
    @POST("Insert_TutorRating")
    fun writeRating(@FieldMap map: HashMap<String, String>): Call<TutorModel>

    @FormUrlEncoded
    @POST("Insert_Test_Hint")
    fun Inserttesthint(@Field("StudentTestID") stutestid: String, @Field("QuestionID") queid: String): Call<JsonObject>

//    @GET("Get_Tutor")
//    fun getTutorList(): Call<PackageData>
//
//    @GET("Get_Subject")
//    fun getSubjectList(): Call<PackageData>

    @GET("Get_TestPackageName_AutoComplete")
    fun getExplore(): Call<PackageData>

//    @GET("Get_Standard")
//    fun getStandardList(): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_TestPackageName_By_Search_Criteria_new")
    fun getFilterData(@FieldMap map: HashMap<String, String>): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_StudentTestPackage_By_Subject")
    fun getMyPackages(
        @Field("StudentID") stuid: String, @Field("SubjectID") subjectid: String, @Field(
            "StandardID"
        ) standardid: String, @Field("IsCompetitive") iscompetitive: String
    ): Call<MyPackageModel>

    @FormUrlEncoded
    @POST("Get_StudentTestPackage_By_Subject_2")
    fun getMyPackages2(
        @Field("StudentID") stuid: String, @Field("SubjectID") subjectid: String, @Field(
            "StandardID") standardid: String, @Field("IsCompetitive") iscompetitive: String,
        @Field("BoardID") boardid: String, @Field("CourseID") courseid: String
    ): Call<MyPackageModel>

    @FormUrlEncoded
    @POST("Get_Course_List")
    fun getExamList(@Field("CourseTypeID") coursetype: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Submit_Test")
    fun submitTest(@Field("StudentTestID") studenttest_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Insert_Test_Answer")
    fun submitAnswer(@FieldMap map: HashMap<String, String>): Call<JsonObject>

//    @FormUrlEncoded
//    @POST("Get_Student_StudentTestAnswer_New")
//    fun getSolution(@Field("TestID") test_id: String, @Field("StudentTestID") student_test_id: String): Call<NewQuestionResponse>

    @FormUrlEncoded
    @POST("Get_StudentTestAnswer_Report")
    fun getAnalyse(@Field("StudentTestID") student_test_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("ReSendOTP")
    fun getResedOTP(@Field("studentmobile") StudentMobile: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Verify_Account_Before_Registration")
    fun verifyAccount(@Field("StudentMobile") mobile_number: String, @Field("StudentEmailAddress") email: String): Call<JsonObject>

//    @FormUrlEncoded
//    @POST("Check_Verify_Account")
//    fun checkVerifyAccount(@Field("MobileNumber") mobile_number: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Add_Cart")
    fun addToCart(@Field("StudentID") student_id: String, @Field("TestPackageID") testpkgid: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_Cart")
    fun getCart(@Field("StudentID") student_id: String): Call<JsonObject>

    @GET("GetCouponList")
    fun getCoupons(): Call<CouponModel>

    @FormUrlEncoded
    @POST("CheckOut")
    fun checkout(@Field("StudentID") student_id: String, @Field("CouponCode") ccode: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("SubscriptionCheckOut")
    fun subscription_checkout(@Field("PaymentAmount") payment_amount: String,
                              @Field("PaymentTransactionID") payment_trans_id: String, @Field("StudentID") student_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Update_Subscription_Payment_Request")
    fun updatesubscriptionPayment(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Insert_IssueReport")
    fun reportIssue(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_TestPackage_Instruction")
    fun getPackageInstruction(@Field("StudentTestPackageID") testpkgid: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Pre_Submit_Test")
    fun attemptReport(@Field("StudentTestID") studenttest_id: String): Call<AttemptModel>

    @FormUrlEncoded
    @POST("Subject_wise_marks")
    fun getSubjectwiseMarks(@Field("StudentTestID") studenttest_id: String): Call<AttemptModel>

    @FormUrlEncoded
    @POST("Send_Enquiry")
    fun sendEnquiry(@Field("FirstName") firstname: String, @Field("LastName") lastname: String, @Field("Email") email: String, @Field("mobile") mobile: String, @Field("Comment") comment: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_StudentTest_SW_Analysis")
    fun studentAnalysis(@Field("StudentTestID") studenttest_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("GetStudentCode")
    fun getRefrenceCode(@Field("StudentID") student_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("ValidateCouponCode")
    fun getValidateCouponCode(@Field("StudentID") student_id: String, @Field("CouponCode") coupon_code: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_GuestUser_Test")
    fun getDplinkTest(@Field("StudentID") student_id: String): Observable<TestListModel>

    @FormUrlEncoded
    @POST("Get_DeepLink")
    fun getDplinkSubject(@Field("DeeplinkGUID") dplink_guid: String, @Field("StudentID") student_id: String): Call<DeepLinkModel>

    @FormUrlEncoded
    @POST("Add_Student_Token")
    fun setFCMToken(@Field("StudentID") student_id: String, @Field("TokenID") token_id: String, @Field("DeviceID") device_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Insert_StudentSubscription_Temp")
    fun insertSubscriptionSubject(@Field("StudentID") student_id: String, @Field("CourseID") course_id: String,
                                  @Field("BoardID") board_id: String, @Field("StandardID") std_id: String,
                                  @Field("TypeID") type_id: String, @Field("IsFree") isfree: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_Subcription_Course_Price")
    fun getSubscriptionPrice(@Field("StudentID") student_id: String, @Field("CourseID") course_id: String,
                             @Field("BoardID") board_id: String, @Field("StandardID") std_id: String,
                             @Field("TypeID") type_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Check_Subcription_FreeTrial")
    fun checkSubscription(@Field("StudentID") student_id: String): Call<GetSubscriptionModel>

    @FormUrlEncoded
    @POST("Get_StudentSubscriptionTemp")
    fun getStudentSubscription(@Field("StudentID") student_id: String): Call<GetSubscriptionModel>

    @FormUrlEncoded
    @POST("Get_ContentCountTemp")
    fun getSubscriptionCount(@Field("StudentID") student_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Confirm_StudentSubscription")
    fun getSubscriptionConfirm(@Field("StudentID") student_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Remove_StudentSubscription_Temp")
    fun removeSubscriptionSubject(@Field("StudentID") student_id: String, @Field("CourseID") course_id: String,
                                  @Field("BoardID") board_id: String,  @Field("StandardID") std_id: String,
                                  @Field("TypeID") type_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Create_SelfTest")
    fun callCreateTest(@FieldMap hasmap: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Create_SelfTest_CE")
    fun callCompetitiveCreateTest(@FieldMap hasmap: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_ChapterList")
    fun callChapterList(@FieldMap hasmap: HashMap<String, String>): Call<GetChapterList>

    @FormUrlEncoded
    @POST("Get_SelfTestList")
    fun callGetSelfTest(@FieldMap hasmap: HashMap<String, String>): Call<GetSelfTest>

    @FormUrlEncoded
    @POST("Get_SelfTest_QueLimit_Board")
    fun callGetQueLimit(@FieldMap hasmap: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_Course_Subject_List")
    fun callCreateTestSubject(@FieldMap hasmap: HashMap<String, String>): Call<GetChapterList>

    @FormUrlEncoded
    @POST("Get_Course_SectionTemplate_List")
    fun callTemplate(@FieldMap hasmap: HashMap<String, String>): Call<GetChapterList>

    @FormUrlEncoded
    @POST("Get_Template_Detail")
    fun callTemplateSection(@FieldMap hasmap: HashMap<String, String>): Call<TemplateSectionModel>

    @FormUrlEncoded
    @POST("Check_StudentSubscription_By_Package")
    fun callGetDiscount(@Field("PackageID") pkg_id: String, @Field("StudentID") stu_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Check_Email_Mobile_By_Type_New")
    fun callCheckDuplicate(@Field("StudentID") stu_id: String, @Field("TypeID") type_id: String,
                           @Field("Email") email: String, @Field("Mobile") phone: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_AppRating")
    fun callGetAppRating(@Field("StudentID") stu_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Insert_AppRating")
    fun callInsertAppRating(@Field("StudentID") stu_id: String,
                            @Field("Rating") rating: String, @Field("Remark") remark: String): Call<JsonObject>

}

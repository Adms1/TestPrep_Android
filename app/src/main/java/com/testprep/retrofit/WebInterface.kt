package com.testprep.retrofit

import com.google.gson.JsonObject
import com.testprep.models.*
import com.testprep.old.models.QuestionResponse
import com.testprep.sectionmodule.NewQuestionResponse
import retrofit2.Call
import retrofit2.http.*

interface WebInterface {

    @FormUrlEncoded
    @POST("Add_Student_Mobile")
    fun getSignup(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_Student_Login")
    fun getLogin(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("SP_Check_Student_Duplicate_Email")
    fun checkEmail(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Update_Student")
    fun updateProfile(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Forgot_Password")
    fun forgotPassword(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Change_Password")
    fun changePassword(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @GET("Get_CourseType_List")
    fun getCourseList(): Call<PackageData>

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
    @POST("GeneratePaymentRequest")
    fun getPayment(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Update_Payment_Request")
    fun updatePaymentStatus(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_TestPackageName_By_ID")
    fun getPackage(@FieldMap map: HashMap<String, String>): Call<GetMarketPlaceData>

    @FormUrlEncoded
    @POST("Get_TestPackage_By_ID")
    fun getPackageDetail(@Field("TestPackageID") testPackageID: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_Student_PaymentTransaction_List")
    fun getMyPayment(@Field("StudentID") boardid: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Add_StudentTestPackage")
    fun addTestPackage(@Field("StudentID") stuid: String, @Field("TestPackageID") pkgid: String): Call<JsonObject>

//    @FormUrlEncoded
//    @POST("Get_StudentTestPackage")
//    fun myTestPackage(@Field("StudentID") stuid: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_StudentTest")
    fun getTestList(@Field("StudentID") stuid: String, @Field("StudentTestPackageID") StudentTestPackageID: String): Call<TestListModel>

    @FormUrlEncoded
    @POST("Get_Student_TestQuestion")
    fun getQuestions(@Field("TestID") testid: String, @Field("StudentTestID") stu_testid: String): Call<QuestionResponse>

    @FormUrlEncoded
    @POST("Get_Student_StudentTestAnswer_New_aws")
    fun getNewQuestions(@Field("TestID") testid: String, @Field("StudentTestID") stu_testid: String): Call<NewQuestionResponse>

    @FormUrlEncoded
    @POST("Get_Tutor_By_TutorID")
    fun getTutorProfile(@Field("TutorID") stuid: String): Call<TutorModel>

    @FormUrlEncoded
    @POST("Get_TestPackageName_By_TutorID")
    fun getTutorSimilarPkgs(@Field("TutorID") stuid: String): Call<PackageData>

    @GET("Get_Tutor")
    fun getTutorList(): Call<PackageData>

    @GET("Get_Subject")
    fun getSubjectList(): Call<PackageData>

    @GET("Get_TestPackageName_AutoComplete")
    fun getExplore(): Call<PackageData>

    @GET("Get_Standard")
    fun getStandardList(): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_TestPackageName_By_Search_Criteria_new")
    fun getFilterData(@FieldMap map: HashMap<String, String>): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_StudentTestPackage_By_Subject")
    fun getMyPackages(@Field("StudentID") stuid: String, @Field("SubjectID") subjectid: String, @Field("IsCompetitive") iscompetitive: String): Call<MyPackageModel>

    @FormUrlEncoded
    @POST("Get_Course_List")
    fun getExamList(@Field("CourseTypeID") coursetype: String): Call<PackageData>

    @FormUrlEncoded
    @POST("Submit_Test")
    fun submitTest(@Field("StudentTestID") studenttest_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Insert_Test_Answer")
    fun submitAnswer(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_Student_StudentTestAnswer_New")
    fun getSolution(@Field("TestID") test_id: String, @Field("StudentTestID") student_test_id: String): Call<NewQuestionResponse>

    @FormUrlEncoded
    @POST("Get_StudentTestAnswer_Report")
    fun getAnalyse(@Field("StudentTestID") student_test_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("ReSendOTP")
    fun getResedOTP(@Field("studentmobile") StudentMobile: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Verify_Account_Before_Registration")
    fun verifyAccount(@Field("StudentMobile") mobile_number: String, @Field("StudentEmailAddress") email: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Check_Verify_Account")
    fun checkVerifyAccount(@Field("MobileNumber") mobile_number: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Add_Cart")
    fun addToCart(@Field("StudentID") student_id: String, @Field("TestPackageID") testpkgid: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("CheckOut")
    fun checkout(@Field("StudentID") student_id: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("Insert_IssueReport")
    fun reportIssue(@FieldMap map: HashMap<String, String>): Call<JsonObject>
}

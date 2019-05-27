package com.testprep.retrofit

import com.google.gson.JsonObject
import com.testprep.models.MainModel
import com.testprep.models.PackageData
import retrofit2.Call
import retrofit2.http.*

interface WebInterface {

    @FormUrlEncoded
    @POST("Add_Student")
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

    @GET("Get_CourseType_List")
    fun getCourseList(): Call<MainModel>

    @FormUrlEncoded
    @POST("Get_Course_List")
    fun getCourseTypeList(@Field("CourseTypeID") coursetype: String): Call<MainModel>

    @FormUrlEncoded
    @POST("Get_CourseSubject_List")
    fun getCourseSubjectList(@Field("CourseID") courseid: String): Call<MainModel>

    @FormUrlEncoded
    @POST("Get_BoardStandard_List")
    fun getBoardStandardList(@Field("BoardID") boardid: String): Call<MainModel>

    @FormUrlEncoded
    @POST("Get_BoardStandardSubject_List")
    fun getBoardStandardSubjectList(@Field("BoardID") boardid: String, @Field("StandardID") standardid: String): Call<MainModel>

    @FormUrlEncoded
    @POST("GeneratePaymentRequest")
    fun getPayment(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Update_Payment_Request")
    fun updatePaymentStatus(@FieldMap map: HashMap<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("Get_TestPackageName_By_ID")
    fun getPackage(@FieldMap map: HashMap<String, String>): Call<PackageData>

    @FormUrlEncoded
    @POST("Get_Student_PaymentTransaction_List")
    fun getMyPayment(@Field("StudentID") boardid: String): Call<PackageData>

}

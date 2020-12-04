package com.quynhlamryan.crm.data

import com.quynhlamryan.crm.data.model.Account
import com.quynhlamryan.crm.data.model.Article
import com.quynhlamryan.crm.data.model.ResponseResult
import com.quynhlamryan.crm.data.model.Store
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiInterface {
    @GET("api/Users/getuserinfo")
    fun getUserInfo(): Call<ResponseResult<Account>>

    @GET("api/Users/sendsmsactivecode/")
    fun getOtp(@Query("phoneNumber") phoneNumber: String): Call<ResponseResult<String>>

    @GET("api/Users/checksmsactivecode/")
    fun verifyOtp(): Call<ResponseResult<Account>>

    @GET("api/CRMNews/getlistnew/")
    fun getListArticles(): Call<ResponseResult<List<Article>>>

    @GET("api/CRMNews/getlistpromotion/")
    fun getListPromotions(): Call<ResponseResult<Account>>

    @GET("api/CRMStores/getliststore/")
    fun getListStores(): Call<ResponseResult<List<Store>>>

    @GET("api/CRMConfig/getlistconfig/")
    fun getConfig(): Call<ResponseResult<Account>>

    @PUT("api/Users/updateuserprofile/")
    fun updateUserProfile(): Call<ResponseResult<Account>>

    @POST("api/Users/uploadavatar/")
    fun uploadAvatar(): Call<ResponseResult<Account>>

    @POST("api/CRMCFMTokens/addfcmtoken/")
    fun addFcmToken(): Call<ResponseResult<Account>>

    @GET("api/Users/getlistmymessage/")
    fun getInbox(): Call<ResponseResult<Account>>
}
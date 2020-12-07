package com.quynhlamryan.crm.data

import com.quynhlamryan.crm.data.model.*
import com.quynhlamryan.crm.data.request.AccountRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("api/Users/getuserinfo")
    fun getUserInfo(): Call<ResponseResult<Account>>

    @GET("api/Users/sendsmsactivecode/")
    fun getOtp(@Query("phoneNumber") phoneNumber: String): Call<ResponseResult<String>>

    @GET("api/Users/checksmsactivecode/")
    fun verifyOtp(@Query("gui") gui: String,
                  @Query("code") smsCode: String,
                  @Query("phone") phone: String,
                  @Query("userUID") userUID: String): Call<ResponseResult<String>>

    @GET("api/CRMNews/getlistnew/")
    fun getListArticles(): Call<ResponseResult<List<Article>>>

    @GET("api/CRMNews/getlistpromotion/")
    fun getListPromotions(): Call<ResponseResult<Account>>

    @GET("api/CRMStores/getliststore/")
    fun getListStores(): Call<ResponseResult<List<Store>>>

    @GET("api/CRMConfig/getlistconfig/")
    fun getConfig(): Call<ResponseResult<Config>>

    @PUT("api/Users/updateuserprofile/")
    fun updateUserProfile(@Body request: AccountRequest): Call<ResponseResult<Boolean>>

    @POST("api/Users/uploadavatar/")
    fun uploadAvatar(): Call<ResponseResult<Account>>

    @POST("api/CRMCFMTokens/addfcmtoken/")
    fun addFcmToken(): Call<ResponseResult<Account>>

    @GET("api/Users/getlistmymessage/")
    fun getInbox(): Call<ResponseResult<Account>>

    @GET("api/CRMPointPOS/viewlisttransactionhistory/")
    fun getListTransactions(): Call<ResponseResult<TransactionHistory>>
}
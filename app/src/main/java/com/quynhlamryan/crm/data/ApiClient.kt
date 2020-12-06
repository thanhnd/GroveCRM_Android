package com.quynhlamryan.crm.data

import com.quynhlamryan.crm.BuildConfig
import com.quynhlamryan.crm.Constants.BASE_URL
import com.quynhlamryan.crm.utils.AccountManager
import com.quynhlamryan.crm.utils.AuthenticationInterceptor
import com.quynhlamryan.crm.utils.resettableLazy
import com.quynhlamryan.crm.utils.resettableManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


internal object ApiClient {
    val lazyMgr = resettableManager()
    private val retrofitClient: Retrofit.Builder by resettableLazy(lazyMgr) {

        val levelType =
        if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
            HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        val okhttpClient = OkHttpClient.Builder()
        AccountManager.token?.let {token ->
            okhttpClient.addInterceptor(AuthenticationInterceptor(token))
        }
        okhttpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiInterface by resettableLazy(lazyMgr) {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }
}
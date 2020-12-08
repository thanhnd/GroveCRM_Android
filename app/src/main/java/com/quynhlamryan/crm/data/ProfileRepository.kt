package com.quynhlamryan.crm.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.quynhlamryan.crm.data.model.Account
import com.quynhlamryan.crm.data.model.ResponseResult
import com.quynhlamryan.crm.data.request.AccountRequest
import com.quynhlamryan.crm.utils.Logger
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProfileRepository {
    val ldResult = MutableLiveData<Boolean>()
    val ldAccount = MutableLiveData<Account>()

    fun updateProfile(request: AccountRequest): MutableLiveData<Boolean> {

        val call = ApiClient.apiInterface.updateUserProfile(request)

        call.enqueue(object: Callback<ResponseResult<Boolean>> {
            override fun onFailure(call: Call<ResponseResult<Boolean>>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseResult<Boolean>>,
                response: Response<ResponseResult<Boolean>>
            ) {
                ldResult.value = response.body()?.isSuccessed
            }
        })

        return ldResult
    }

    fun uploadAvatar(request: MultipartBody.Part): MutableLiveData<Boolean> {

        val call = ApiClient.apiInterface.uploadAvatar(request)

        call.enqueue(object: Callback<ResponseResult<Boolean>> {
            override fun onFailure(call: Call<ResponseResult<Boolean>>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseResult<Boolean>>,
                response: Response<ResponseResult<Boolean>>
            ) {
                ldResult.value = response.body()?.isSuccessed
            }
        })

        return ldResult
    }

    fun getAccount(): MutableLiveData<Account> {

        val call = ApiClient.apiInterface.getUserInfo()

        call.enqueue(object: Callback<ResponseResult<Account>> {
            override fun onFailure(call: Call<ResponseResult<Account>>, t: Throwable) {
                Logger.d(t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseResult<Account>>,
                response: Response<ResponseResult<Account>>
            ) {
                Logger.d(response.body().toString())
                ldAccount.value = response.body()?.resultObj
            }
        })

        return ldAccount
    }
}

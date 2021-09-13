package com.grove.crm.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.grove.crm.data.model.ResponseResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

object InputOtpRepository {

    val ldOtp = MutableLiveData<String>()

    fun verifyOTP(gui: String, smsCode: String, phoneNumber: String, userUID: String): MutableLiveData<String> {

        val call = ApiClient.apiInterface.verifyOtp(gui, smsCode, phoneNumber, userUID)

        call.enqueue(object: Callback<ResponseResult<String>> {
            override fun onFailure(call: Call<ResponseResult<String>>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseResult<String>>,
                response: Response<ResponseResult<String>>
            ) {
                Log.v("DEBUG : ", response.body().toString())
                ldOtp.value = response.body()?.resultObj
            }
        })

        return ldOtp
    }
}
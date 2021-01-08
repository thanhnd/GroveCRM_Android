package com.quynhlamryan.crm.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.quynhlamryan.crm.data.model.Notify
import com.quynhlamryan.crm.data.model.ResponseResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NotifyRepository {
    fun getListNotify(): MutableLiveData<Notify> {

        val call = ApiClient.apiInterface.getInbox()
        val ldNotify = MutableLiveData<Notify>()
        call.enqueue(object: Callback<ResponseResult<Notify>> {
            override fun onFailure(call: Call<ResponseResult<Notify>>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseResult<Notify>>,
                response: Response<ResponseResult<Notify>>
            ) {
                ldNotify.value = response.body()?.resultObj
            }
        })

        return ldNotify
    }

}

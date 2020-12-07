package com.quynhlamryan.crm.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.quynhlamryan.crm.data.model.Config
import com.quynhlamryan.crm.data.model.ResponseResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SettingRepository {
    val ldConfig = MutableLiveData<Config>()

    fun getConfig(): MutableLiveData<Config> {

        val call = ApiClient.apiInterface.getConfig()

        call.enqueue(object: Callback<ResponseResult<Config>> {
            override fun onFailure(call: Call<ResponseResult<Config>>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseResult<Config>>,
                response: Response<ResponseResult<Config>>
            ) {
                ldConfig.value = response.body()?.resultObj
            }
        })

        return ldConfig
    }



}

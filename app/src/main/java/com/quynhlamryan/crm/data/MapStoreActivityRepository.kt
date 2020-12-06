package com.quynhlamryan.crm.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.quynhlamryan.crm.data.model.ResponseResult
import com.quynhlamryan.crm.data.model.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MapStoreActivityRepository {
    val stores = MutableLiveData<List<Store>>()

    fun getListStores(): MutableLiveData<List<Store>> {

        val call = ApiClient.apiInterface.getListStores()

        call.enqueue(object: Callback<ResponseResult<List<Store>>> {
            override fun onFailure(call: Call<ResponseResult<List<Store>>>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseResult<List<Store>>>,
                response: Response<ResponseResult<List<Store>>>
            ) {
                Log.v("DEBUG : ", response.body().toString())

                stores.value = response.body()?.resultObj
            }
        })

        return stores
    }
}
package com.quynhlamryan.crm.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.quynhlamryan.crm.data.model.Article
import com.quynhlamryan.crm.data.model.ResponseResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainActivityRepository {
    val serviceSetterGetter = MutableLiveData<List<Article>>()

    fun getServicesApiCall(): MutableLiveData<List<Article>> {

        val call = ApiClient.apiInterface.getListArticles()

        call.enqueue(object: Callback<ResponseResult<List<Article>>> {
            override fun onFailure(call: Call<ResponseResult<List<Article>>>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseResult<List<Article>>>,
                response: Response<ResponseResult<List<Article>>>
            ) {
                // TODO("Not yet implemented")
                Log.v("DEBUG : ", response.body().toString())

                val data = response.body()

                val articles = data?.resultObj

                serviceSetterGetter.value = data?.resultObj
            }
        })

        return serviceSetterGetter
    }
}
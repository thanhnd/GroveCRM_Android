package com.quynhlamryan.crm.data

import androidx.lifecycle.MutableLiveData
import com.quynhlamryan.crm.data.model.Article
import com.quynhlamryan.crm.data.model.ResponseResult
import com.quynhlamryan.crm.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainActivityRepository {

    val ldArticles = MutableLiveData<List<Article>>()
    fun getListArticles(): MutableLiveData<List<Article>> {

        val call = ApiClient.apiInterface.getListArticles()

        call.enqueue(object: Callback<ResponseResult<List<Article>>> {
            override fun onFailure(call: Call<ResponseResult<List<Article>>>, t: Throwable) {
                // TODO("Not yet implemented")
                Logger.w(t)
            }

            override fun onResponse(
                call: Call<ResponseResult<List<Article>>>,
                response: Response<ResponseResult<List<Article>>>
            ) {
                // TODO("Not yet implemented")
                Logger.d(response.body().toString())
                ldArticles.value = response.body()?.resultObj
            }
        })

        return ldArticles
    }


}
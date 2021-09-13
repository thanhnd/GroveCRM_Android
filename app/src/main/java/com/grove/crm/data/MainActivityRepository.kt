package com.grove.crm.data

import androidx.lifecycle.MutableLiveData
import com.grove.crm.data.model.Article
import com.grove.crm.data.model.HotNews
import com.grove.crm.data.model.Promotion
import com.grove.crm.data.model.ResponseResult
import com.grove.crm.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainActivityRepository {

    val ldArticles = MutableLiveData<List<Article>>()
    val ldPromotions = MutableLiveData<List<Promotion>>()
    val ldHotNews = MutableLiveData<HotNews>()

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

    fun getHotNews(): MutableLiveData<HotNews> {

        val call = ApiClient.apiInterface.getHotNews()

        call.enqueue(object: Callback<ResponseResult<HotNews>> {
            override fun onFailure(call: Call<ResponseResult<HotNews>>, t: Throwable) {
                // TODO("Not yet implemented")
                Logger.w(t)
            }

            override fun onResponse(
                call: Call<ResponseResult<HotNews>>,
                response: Response<ResponseResult<HotNews>>
            ) {
                // TODO("Not yet implemented")
                Logger.d(response.body().toString())
                ldHotNews.value = response.body()?.resultObj
            }
        })

        return ldHotNews
    }

    fun getListPromotions(): MutableLiveData<List<Promotion>> {

        val call = ApiClient.apiInterface.getListPromotions()

        call.enqueue(object: Callback<ResponseResult<List<Promotion>>> {
            override fun onFailure(call: Call<ResponseResult<List<Promotion>>>, t: Throwable) {
                // TODO("Not yet implemented")
                Logger.w(t)
            }

            override fun onResponse(
                call: Call<ResponseResult<List<Promotion>>>,
                response: Response<ResponseResult<List<Promotion>>>
            ) {
                // TODO("Not yet implemented")
                Logger.d(response.body().toString())
                ldPromotions.value = response.body()?.resultObj
            }
        })

        return ldPromotions
    }
}
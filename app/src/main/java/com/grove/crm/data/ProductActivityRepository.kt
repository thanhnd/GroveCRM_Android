package com.grove.crm.data

import androidx.lifecycle.MutableLiveData
import com.grove.crm.data.model.Product
import com.grove.crm.data.model.ResponseResult
import com.grove.crm.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProductActivityRepository {

    val ldProducts = MutableLiveData<List<Product>>()

    fun getListProducts(): MutableLiveData<List<Product>> {

        val call = ApiClient.apiInterface.getListProducts()

        call.enqueue(object : Callback<ResponseResult<List<Product>>> {
            override fun onFailure(call: Call<ResponseResult<List<Product>>>, t: Throwable) {
                // TODO("Not yet implemented")
                Logger.w(t)
            }

            override fun onResponse(
                call: Call<ResponseResult<List<Product>>>,
                response: Response<ResponseResult<List<Product>>>
            ) {
                Logger.d(response.body().toString())
                ldProducts.value = response.body()?.resultObj
            }
        })
        return ldProducts
    }
}
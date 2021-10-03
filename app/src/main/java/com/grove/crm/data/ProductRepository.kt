package com.grove.crm.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.grove.crm.data.model.Order
import com.grove.crm.data.model.Product
import com.grove.crm.data.model.ResponseResult
import com.grove.crm.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProductRepository {

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

    fun submitOrder(order: Order): MutableLiveData<Boolean> {

        val call = ApiClient.apiInterface.submitOrder(order)

        val ldResult = MutableLiveData<Boolean>()
        call.enqueue(object : Callback<ResponseResult<Boolean>> {
            override fun onFailure(call: Call<ResponseResult<Boolean>>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
                ldResult.value = false
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
}
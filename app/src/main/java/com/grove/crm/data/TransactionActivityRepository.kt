package com.grove.crm.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.grove.crm.data.model.ResponseResult
import com.grove.crm.data.model.TransactionHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object TransactionActivityRepository {
    val ldTransaction = MutableLiveData<TransactionHistory>()

    fun getListTransactions(): MutableLiveData<TransactionHistory> {

        val call = ApiClient.apiInterface.getListTransactions()

        call.enqueue(object: Callback<ResponseResult<TransactionHistory>> {
            override fun onFailure(call: Call<ResponseResult<TransactionHistory>>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
                ldTransaction.value = null
            }

            override fun onResponse(
                call: Call<ResponseResult<TransactionHistory>>,
                response: Response<ResponseResult<TransactionHistory>>
            ) {
                ldTransaction.value = response.body()?.resultObj
            }
        })

        return ldTransaction
    }

}

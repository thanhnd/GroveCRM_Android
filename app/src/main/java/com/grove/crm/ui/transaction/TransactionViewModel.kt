package com.grove.crm.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grove.crm.data.TransactionActivityRepository
import com.grove.crm.data.model.TransactionHistory

class TransactionViewModel : ViewModel() {

    var ldTransactions: MutableLiveData<TransactionHistory>? = null

    fun getListTransactions() : LiveData<TransactionHistory>? {
        this.ldTransactions = TransactionActivityRepository.getListTransactions()
        return this.ldTransactions
    }
}
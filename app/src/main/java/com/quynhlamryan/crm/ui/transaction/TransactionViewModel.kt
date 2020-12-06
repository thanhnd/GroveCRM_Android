package com.quynhlamryan.crm.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.TransactionActivityRepository
import com.quynhlamryan.crm.data.model.TransactionHistory

class TransactionViewModel : ViewModel() {

    var ldTransactions: MutableLiveData<TransactionHistory>? = null

    fun getListTransactions() : LiveData<TransactionHistory>? {
        this.ldTransactions = TransactionActivityRepository.getListTransactions()
        return this.ldTransactions
    }
}
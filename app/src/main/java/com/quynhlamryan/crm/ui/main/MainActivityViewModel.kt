package com.quynhlamryan.crm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.MainActivityRepository
import com.quynhlamryan.crm.data.model.Account
import com.quynhlamryan.crm.data.model.Article
import com.quynhlamryan.crm.utils.AccountManager

class MainActivityViewModel : ViewModel() {

    var ldArticles: MutableLiveData<List<Article>>? = null
    var ldAccount: MutableLiveData<Account>? = null

    fun getArticles() : LiveData<List<Article>>? {
        this.ldArticles = MainActivityRepository.getListArticles()
        return this.ldArticles
    }

    fun getAccount() : LiveData<Account>? {
        this.ldAccount = MainActivityRepository.getAccount()
        AccountManager.account = ldAccount?.value
        return this.ldAccount
    }
}
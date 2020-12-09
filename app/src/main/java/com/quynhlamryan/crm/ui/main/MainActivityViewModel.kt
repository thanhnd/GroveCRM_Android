package com.quynhlamryan.crm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.MainActivityRepository
import com.quynhlamryan.crm.data.ProfileRepository
import com.quynhlamryan.crm.data.model.Account
import com.quynhlamryan.crm.data.model.Article
import com.quynhlamryan.crm.data.model.Promotion

class MainActivityViewModel : ViewModel() {

    private var ldArticles: MutableLiveData<List<Article>>? = null
    private var ldPromotions: MutableLiveData<List<Promotion>>? = null
    private var ldAccount: MutableLiveData<Account>? = null

    fun getPromotions() : LiveData<List<Promotion>>? {
        this.ldPromotions = MainActivityRepository.getListPromotions()
        return this.ldPromotions
    }

    fun getArticles() : LiveData<List<Article>>? {
        this.ldArticles = MainActivityRepository.getListArticles()
        return this.ldArticles
    }

    fun getAccount() : LiveData<Account>? {
        this.ldAccount = ProfileRepository.getAccount()
        return this.ldAccount
    }
}
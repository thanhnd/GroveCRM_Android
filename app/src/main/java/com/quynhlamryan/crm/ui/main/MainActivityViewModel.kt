package com.quynhlamryan.crm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.Constants
import com.quynhlamryan.crm.data.MainActivityRepository
import com.quynhlamryan.crm.data.ProfileRepository
import com.quynhlamryan.crm.data.model.Account
import com.quynhlamryan.crm.data.model.Article
import com.quynhlamryan.crm.data.model.Promotion
import com.quynhlamryan.crm.data.request.FcmTokenRequest
import com.quynhlamryan.crm.utils.AccountManager

class MainActivityViewModel : ViewModel() {

    private var ldArticles: MutableLiveData<List<Article>>? = null
    private var ldPromotions: MutableLiveData<List<Promotion>>? = null

    fun getPromotions(): LiveData<List<Promotion>>? {
        this.ldPromotions = MainActivityRepository.getListPromotions()
        return this.ldPromotions
    }

    fun getArticles(): LiveData<List<Article>>? {
        this.ldArticles = MainActivityRepository.getListArticles()
        return this.ldArticles
    }

    fun getAccount(): LiveData<Account>? {
        val ldAccount = MutableLiveData<Account>()
        ProfileRepository.getAccount {account ->
            ldAccount.value = account

            AccountManager.fcmToken?.let {fcmToken ->
                val request = FcmTokenRequest(token = fcmToken,
                    osName = Constants.osName,
                    crmId = account.userName)
                ProfileRepository.postFcmToken(request)
            }

        }

        return ldAccount
    }
}
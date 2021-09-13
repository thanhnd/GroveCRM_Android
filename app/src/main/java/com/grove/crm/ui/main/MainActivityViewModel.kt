package com.grove.crm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grove.crm.Constants
import com.grove.crm.data.MainActivityRepository
import com.grove.crm.data.ProfileRepository
import com.grove.crm.data.model.Account
import com.grove.crm.data.model.Article
import com.grove.crm.data.model.HotNews
import com.grove.crm.data.model.Promotion
import com.grove.crm.data.request.FcmTokenRequest
import com.grove.crm.utils.AccountManager

class MainActivityViewModel : ViewModel() {

    private var ldArticles: MutableLiveData<List<Article>>? = null
    private var ldPromotions: MutableLiveData<List<Promotion>>? = null
    private var ldHotNews: MutableLiveData<HotNews>? = null

    fun getPromotions(): LiveData<List<Promotion>>? {
        this.ldPromotions = MainActivityRepository.getListPromotions()
        return this.ldPromotions
    }

    fun getArticles(): LiveData<List<Article>>? {
        this.ldArticles = MainActivityRepository.getListArticles()
        return this.ldArticles
    }

    fun getHotNew(): LiveData<HotNews>? {
        this.ldHotNews = MainActivityRepository.getHotNews()
        return this.ldHotNews
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
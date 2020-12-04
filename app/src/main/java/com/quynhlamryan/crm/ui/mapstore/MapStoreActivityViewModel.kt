package com.quynhlamryan.crm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.MainActivityRepository
import com.quynhlamryan.crm.data.model.Article

class MainActivityViewModel : ViewModel() {

    var servicesLiveData: MutableLiveData<List<Article>>? = null

    fun getArticles() : LiveData<List<Article>>? {
        servicesLiveData = MainActivityRepository.getServicesApiCall()
        return servicesLiveData
    }

}
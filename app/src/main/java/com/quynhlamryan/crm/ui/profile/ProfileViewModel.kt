package com.quynhlamryan.crm.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.SettingRepository
import com.quynhlamryan.crm.data.model.Config

class ProfileViewModel : ViewModel() {

    var ldConfig: MutableLiveData<Config>? = null

    fun getListTransactions() : LiveData<Config>? {
        this.ldConfig = SettingRepository.getConfig()
        return this.ldConfig
    }
}
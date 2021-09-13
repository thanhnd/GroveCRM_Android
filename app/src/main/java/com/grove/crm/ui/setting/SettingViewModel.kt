package com.grove.crm.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grove.crm.data.ProfileRepository
import com.grove.crm.data.SettingRepository
import com.grove.crm.data.model.Account
import com.grove.crm.data.model.Config

class SettingViewModel : ViewModel() {

    var ldConfig: MutableLiveData<Config>? = null

    fun getListTransactions() : LiveData<Config>? {
        this.ldConfig = SettingRepository.getConfig()
        return this.ldConfig
    }

    fun getAccount(): LiveData<Account>? {
        val ldAccount = MutableLiveData<Account>()
        ProfileRepository.getAccount {
            ldAccount.value = it
        }

        return ldAccount
    }
}
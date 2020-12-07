package com.quynhlamryan.crm.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.ProfileRepository
import com.quynhlamryan.crm.data.request.AccountRequest

class ProfileViewModel : ViewModel() {

    var ldResult: MutableLiveData<Boolean>? = null

    fun updateAccount(name: String?, birthday: String?, email: String?) : LiveData<Boolean>? {

        val request = AccountRequest(name, email, birthday)
        this.ldResult = ProfileRepository.updateProfile(request)
        return this.ldResult
    }
}
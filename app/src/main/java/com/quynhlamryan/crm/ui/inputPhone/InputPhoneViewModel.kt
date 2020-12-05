package com.quynhlamryan.crm.ui.inputPhone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.InputPhoneRepository

class InputPhoneViewModel : ViewModel() {
    var ldOtp: MutableLiveData<String>? = null

    fun getOtp(phoneNumber: String) : LiveData<String>? {
        ldOtp = InputPhoneRepository.getOtp(phoneNumber)
        return ldOtp
    }

}
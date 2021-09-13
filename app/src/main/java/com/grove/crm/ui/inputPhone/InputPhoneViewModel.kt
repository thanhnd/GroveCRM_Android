package com.grove.crm.ui.inputPhone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grove.crm.data.InputPhoneRepository

class InputPhoneViewModel : ViewModel() {

    fun getOtp(phoneNumber: String) : LiveData<String>? {
        val ldOtp: MutableLiveData<String> = MutableLiveData()
        InputPhoneRepository.getOtp(phoneNumber) {
                ldOtp.value = it
        }

        return ldOtp
    }

}
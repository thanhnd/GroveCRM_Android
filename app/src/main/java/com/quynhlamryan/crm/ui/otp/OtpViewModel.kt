package com.quynhlamryan.crm.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.InputOtpRepository
import com.quynhlamryan.crm.data.ProfileRepository
import com.quynhlamryan.crm.data.model.Account
import com.quynhlamryan.crm.utils.AccountManager

class OtpViewModel : ViewModel() {
    var ldToken: LiveData<String>? = null

    fun verifyOtp(userUID: String) : LiveData<String>? {
        val accountCode = AccountManager.accountCode ?: return null
        val phoneNumber = AccountManager.phone ?: return null
        val defaultSmsCode = "123456"
        ldToken =  InputOtpRepository.verifyOTP(accountCode, defaultSmsCode, phoneNumber, userUID)

        return ldToken
    }

    fun getAccount(): LiveData<Account>? {
        val ldAccount = MutableLiveData<Account>()
        ProfileRepository.getAccount {
            ldAccount.value = it
        }

        return ldAccount
    }

}